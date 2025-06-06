package com.sen3006.coursewise.client;

import com.sen3006.coursewise.client.enums.Semester;
import com.sen3006.coursewise.client.models.*;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

public class GUIController implements Initializable, Observer {

    @FXML private TextField searchField;
    @FXML private VBox courseListContainer;

    @FXML private Label courseCodeLabel;
    @FXML private Label courseRatingLabel;
    @FXML private Label courseTitleLabel;
    @FXML private Label sectionTypeLabel;
    @FXML private Label profNameLabel;
    @FXML private Label courseListLabel;
    @FXML private Label profRatingLabel;
    @FXML private Label weekdayLabel;
    @FXML private Label durationLabel;
    @FXML private Label campusLabel;
    @FXML private Label roomLabel;
    @FXML private Label reviewCountLabel;
    @FXML private Label semesterLabel;

    @FXML private VBox reviewsContainer;
    @FXML private VBox sectionsContainer;

    @FXML private TextArea lecturersNoteTextArea;

    private ToggleGroup sectionGroup;
    private Course currentCourse;
    private static boolean creatingNewReview = false;
    private Section currentSection;
    private Semester selectedSemester = Semester.Fall; // Fall, Spring

    private static GUIController instance;
    API api = API.getInstance();

    public static GUIController getInstance() {
        if (instance == null) {
            instance = new GUIController();
        }
        return instance;
    }

    private GUIController(){
        System.out.println("New GUIController instance created");
        if (instance == null) {
            instance = this;
        } else {
            throw new IllegalStateException("Already an instance of GUIController");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the GUI
        if (!GUIController.creatingNewReview) {
            loadCourseList();
            sectionGroup = new ToggleGroup();

            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterCourses(newValue);
            });

            if (!courseListContainer.getChildren().isEmpty()) {
                currentCourse = api.getCourses(selectedSemester)[0];
                loadCourseDetails(currentCourse);
            }

            if (courseListLabel != null) {
                semesterMenu(courseListLabel);
            }
            semesterLabel.setStyle("-fx-background-color: #f3e5f5; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");
            semesterLabel.setText("FALL");
        }
    }

    private void loadCourseList() {
        courseListContainer.getChildren().clear();

        Course[] courses = api.getCourses(selectedSemester);
        for (Course course : courses) {
            if(currentCourse != null && course.getCourse_id().contentEquals(currentCourse.getCourse_id()))
                currentCourse = course;
            addCourseItemToList(course, String.valueOf(course.getAvgRating()));
        }
    }

    private void semesterMenu(Label label) {
        label.setStyle(label.getStyle() + "; -fx-cursor: hand;");

        // Add hover effect
        label.setOnMouseEntered(e -> label.setStyle(label.getStyle() + "; -fx-underline: true;"));
        label.setOnMouseExited(e -> label.setStyle(label.getStyle().replace("; -fx-underline: true;", "")));

        ContextMenu semesterMenu = new ContextMenu();
        semesterMenu.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #cccccc;");

        // SPRING
        Label springLabel = new Label("Spring");
        springLabel.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px; -fx-text-fill: #333;");
        springLabel.setOnMouseClicked(ev -> {
            System.out.println("Spring selected");
            selectedSemester = Semester.Spring;
            semesterLabel.setStyle("-fx-background-color: #d0f0d9; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");
            semesterLabel.setText("SPRING");
            loadCourseList();
            loadCourseDetails(currentCourse);
            loadAvailableSections(currentCourse);
            filterCourses(searchField.getText());
            semesterMenu.hide();
        });

        // FALL
        Label fallLabel = new Label("Fall");
        fallLabel.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px; -fx-text-fill: #333;");
        fallLabel.setOnMouseClicked(ev -> {
            System.out.println("Fall selected");
            selectedSemester = Semester.Fall;
            semesterLabel.setStyle("-fx-background-color: #f3e5f5; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");
            semesterLabel.setText("FALL");
            loadCourseList();
            loadCourseDetails(currentCourse);
            loadAvailableSections(currentCourse);
            filterCourses(searchField.getText());
            semesterMenu.hide();
        });

        // Custom Menu Items
        CustomMenuItem springItem = new CustomMenuItem(new HBox(springLabel));
        CustomMenuItem fallItem = new CustomMenuItem(new HBox(fallLabel));

        // Set the items to be non-removable
        springItem.setHideOnClick(false);
        fallItem.setHideOnClick(false);

        semesterMenu.getItems().addAll(springItem, fallItem);

        // Show the menu when the label is clicked
        label.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                label.setStyle(label.getStyle() + "; -fx-background-color: #e0e0e0;");

                semesterMenu.show(label, Side.BOTTOM, 0, 0);

                PauseTransition pause = new PauseTransition(Duration.millis(200));
                pause.setOnFinished(event -> label.setStyle(label.getStyle().replace("; -fx-background-color: #e0e0e0;", "")));
                pause.play();
            }
        });
    }
    //Loads course list from API or database


    //Add a course item to the list
    private void addCourseItemToList(Course course, String rating) {
        HBox courseItem = new HBox();
        courseItem.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        courseItem.setSpacing(10.0);
        courseItem.setPadding(new Insets(5));

        // Style for a default course item
        if (course.getAvgRating() == 10) {
            courseItem.setStyle("-fx-background-color: #f3e5f5; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");
        } else if (course.getAvgRating()  == 9) {
            courseItem.setStyle("-fx-background-color: #d0f0d9; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-radius: 4;"); // mor-yeşil arası
        } else if (course.getAvgRating() == 8) {
            courseItem.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");
        } else if (course.getAvgRating()== 7) {
            courseItem.setStyle("-fx-background-color: #f1f8e9; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");
        } else if (course.getAvgRating() == 6) {
            courseItem.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");
        } else if (course.getAvgRating() == 5) {
            courseItem.setStyle("-fx-background-color: #fff3e0; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");
        } else if (course.getAvgRating() == 4) {
            courseItem.setStyle("-fx-background-color: #ffecec; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");
        }
        else if (course.getAvgRating() == 3) {
            courseItem.setStyle("-fx-background-color: #ffefef; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-radius: 4;"); // en açık kırmızı
        }
        else if (course.getAvgRating() == 2) {
        courseItem.setStyle("-fx-background-color: #ffdddd; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-radius: 4;"); // daha açık kırmızı
        }
        else if (course.getAvgRating() == 1) {
            courseItem.setStyle("-fx-background-color: #ffc9c9; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");
        }
        else {
            courseItem.setStyle("-fx-background-color: aliceblue; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");
        }


        Label codeLabel = new Label(course.getCourse_id());
        codeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: black;");

        Label ratingLabel = new Label(rating + "/10");
        ratingLabel.setStyle("-fx-border-color: black; -fx-border-radius: 10; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: lightblue;-fx-font-weight: bold");
        ratingLabel.setPadding(new Insets(2, 5, 2, 5));
        ratingLabel.setMinWidth(40);

        courseItem.getChildren().addAll(codeLabel, ratingLabel);

        // Add click event to load course details
        courseItem.setOnMouseClicked(event -> {
            loadCourseDetails(api.getCourse(course.getCourse_id()));
            loadCourseList();
            filterCourses(searchField.getText());
        });
        courseItem.setStyle(courseItem.getStyle() + "; -fx-cursor: hand;");

        // Add hover effect
        courseItem.setOnMouseEntered(event ->
                courseItem.setStyle(courseItem.getStyle().replace("-fx-background-color: white;", "-fx-background-color: #f5f5f5;")));

        courseItem.setOnMouseExited(event ->
                courseItem.setStyle(courseItem.getStyle().replace("-fx-background-color: #f5f5f5;", "-fx-background-color: white;")));

        courseListContainer.getChildren().add(courseItem);
    }

    //Filter courses based on search text
    private void filterCourses(String searchText) {
        // Filtering will be done when the user types at least 3 characters
        if (searchText.length() < 3 && !searchText.isEmpty()) {
            return;
        }

        Course[] courses = api.getCourses(selectedSemester); // Get courses from API

        // Clear the list first
        courseListContainer.getChildren().clear();

        // Iterate through the courses and add the ones matching the searchText
        boolean foundAny = false;
        for (Course course : courses) {
            if (course.getCourse_id().toLowerCase().contains(searchText.toLowerCase())) {
                // Add matching course to the GUI
                addCourseItemToList(course, String.valueOf(course.getAvgRating()));
                foundAny = true;
            }
        }

        // If no results were found, show a message to the user
        if (!foundAny) {
            showNoResultsMessage();
        }
    }

    // Message to display when no results are found
    private void showNoResultsMessage() {
        Label noResultsLabel = new Label("No courses found matching the search criteria");
        noResultsLabel.setStyle("-fx-text-fill: #757575; -fx-padding: 10;");
        courseListContainer.getChildren().add(noResultsLabel);
    }

    //Load course details when a course is selected
    private void loadCourseDetails(Course course) {
        // Save current course code
        currentCourse = course;
        if (course != null) {
            // Update course details UI
            courseTitleLabel.setText(course.getCourse_name());
            courseRatingLabel.setText(course.getAvgRating() + "/10");
            courseCodeLabel.setText(course.getCourse_id());
        }

        // Load available sections
        loadAvailableSections(course);
        loadLecturersNote();

        // If sections are available, load the first section by default
        if (!sectionsContainer.getChildren().isEmpty()) {
            RadioButton firstSection = (RadioButton) sectionsContainer.getChildren().get(0);
            firstSection.setSelected(true);
            loadSectionDetails(currentSection);
        }
    }

    //Load available sections for a course
    private void loadAvailableSections(Course course) {
        sectionsContainer.getChildren().clear();
        sectionGroup = new ToggleGroup();

        Section[] sections = api.getSections(course.getCourse_id(), selectedSemester); // This should return the list of sections for the course
        for (Section section : sections) {
            addSectionToList(section);
        }
        // Set the current section to the first one in the list
        if (sections.length > 0) {
            currentSection = sections[0];
        }

        // Select first section by default
        if (!sectionsContainer.getChildren().isEmpty()) {
            RadioButton firstSection = (RadioButton) sectionsContainer.getChildren().get(0);
            firstSection.setSelected(true);
            loadSectionDetails(currentSection);
        }
    }

    //Add a section to the sections list
    private void addSectionToList(Section section) {
        String sectionCode = String.valueOf(section.getSection_id());
        RadioButton radioButton = new RadioButton(currentCourse.getCourse_id() + "(" + sectionCode + ")");
        radioButton.setToggleGroup(sectionGroup);

        // Style the radio button
        if (sectionCode.endsWith("(1)")) {
            radioButton.setStyle("-fx-text-fill: #0d47a1; -fx-font-weight: bold;");
            radioButton.setSelected(true);
        } else {
            radioButton.setStyle("-fx-text-fill: #424242;");
        }

        radioButton.setPadding(new Insets(2));

        // Add click event
        radioButton.setOnAction(event -> loadSectionDetails(section));

        sectionsContainer.getChildren().add(radioButton);
    }

    //Load section details when a section is selected
    private void loadSectionDetails(Section section) {
        this.currentSection = section;
        courseCodeLabel.setText(currentCourse.getCourse_id());

        weekdayLabel.setText(currentSection.getSection_day().toString());
        durationLabel.setText(currentSection.getStart_time().toString() + " - " + currentSection.getEnd_time().toString());
        roomLabel.setText(currentSection.getClassroom().getClass_id());
        profNameLabel.setText(currentSection.getProfessor().getProf_name() + " " + currentSection.getProfessor().getSurname());
        profRatingLabel.setText(currentSection.getProfessor().getAvgRating() + "/10");
        campusLabel.setText(String.valueOf(currentSection.getClassroom().getCampus()));
        sectionTypeLabel.setText(currentSection.getType().toString());

        // Load reviews for this course
        loadReviews(currentCourse);
    }

    //Load reviews of a course, not a section
    private void loadReviews(Course course) {
        reviewsContainer.getChildren().clear();

         Review[] reviews = api.getReviews(course.getCourse_id());
         updateReviewCountLabel(reviews.length);
         for (Review review : reviews) {
             addReviewToContainer(review);
         }
    }

    private void addReviewToContainer(Review review) {
        String reviewer = String.valueOf(review.getUser().getId());
        String rating = (review.getRating()) + "/10";
        String comment = review.getComment();

        String bgColor = "";
        String textColor = "";

        if (review.getRating() == 10) {
            bgColor = "#f3e5f5"; // purple
            textColor = "#6a1b9a";
        } else if (review.getRating() == 9) {
            bgColor = "#d0f0d9"; // green
            textColor = "#1565c0";
        } else if (review.getRating() == 8) {
            bgColor = "#e8f5e9"; // less gamma green
            textColor = "#2e7d32";
        } else if (review.getRating() == 7) {
            bgColor = "#f1f8e9"; // lesser gamma green
            textColor = "#558b2f";
        } else if (review.getRating() == 6) {
            bgColor = "#fffde7"; // yellow
            textColor = "#f9a825";
        } else if (review.getRating() == 5) {
            bgColor = "#fff3e0"; // orange
            textColor = "#ef6c00";
        } else if (review.getRating() == 4) {
            bgColor = "#ffecec"; // red
            textColor = "#e53935";
        } else if (review.getRating() == 3) {
            bgColor = "#ffefef"; // even more gamma red
            textColor = "#d32f2f";
        } else if (review.getRating() == 2) {
            bgColor = "#ffdddd"; // more gamma red
            textColor = "#c62828";
        } else if (review.getRating() == 1) {
            bgColor = "#ffc9c9"; // red
            textColor = "#b71c1c";
        }


        VBox reviewBox = new VBox(5);
        reviewBox.setStyle("-fx-background-color: %s; -fx-background-radius: 4; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 2, 0, 0, 1);".formatted(bgColor));
        reviewBox.setPadding(new Insets(5));

        HBox reviewHeader = new HBox(10);
        reviewHeader.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label reviewUser = new Label("→ " + reviewer + ",");
        reviewUser.setStyle("-fx-text-fill:%s;".formatted(textColor));

        Label reviewRating = new Label(rating);
        reviewRating.setStyle("-fx-border-color:%s; -fx-border-radius: 10; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: %s;".formatted(textColor, textColor));
        reviewRating.setPadding(new Insets(2, 5, 2, 5));

        reviewHeader.getChildren().addAll(reviewUser, reviewRating);

        Label reviewContent = new Label(comment);
        reviewContent.setStyle("-fx-text-fill: #212121;");
        reviewContent.setWrapText(true);

        reviewBox.getChildren().addAll(reviewHeader, reviewContent);

        reviewsContainer.getChildren().add(reviewBox);

        // Add separator except for the last item
        if (!reviewsContainer.getChildren().isEmpty() &&
                reviewsContainer.getChildren().indexOf(reviewBox) < reviewsContainer.getChildren().size() - 1) {
            Separator separator = new Separator();
            separator.setStyle("-fx-background-color: #e0e0e0;");
            reviewsContainer.getChildren().add(separator);
        }
    }

    //Show dialog to add a new review
    @FXML
    private void showAddReviewDialog(ActionEvent event) throws IOException {
        GUIController.creatingNewReview = true;

        FXMLLoader dialogLoader = new FXMLLoader();
        dialogLoader.setController(GUIController.getInstance()); // Assign the singleton instance of GUIController as the controller
        dialogLoader.setLocation(getClass().getResource("/com/sen3006/coursewise/client/ReviewDialog.fxml"));
        DialogPane dialogPane = dialogLoader.load();

        // Create the dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Add Review for " + currentCourse.getCourse_id());
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Get the dialog components
        Label studentIdLabel = (Label) dialogPane.lookup("#studentIdLabel");
        Slider ratingSlider = (Slider) dialogPane.lookup("#ratingSlider");
        Label ratingValueLabel = (Label) dialogPane.lookup("#ratingValueLabel");
        TextArea reviewTextArea = (TextArea) dialogPane.lookup("#reviewTextArea");
        Label characterCountLabel = (Label) dialogPane.lookup("#characterCountLabel");

        // Get the student ID from the current user session and set it to the label
        studentIdLabel.setText(String.valueOf(CurrentUser.getInstance().getId()));

        // Set up character limit for review text area (300 characters)
        reviewTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 300) {
                reviewTextArea.setText(oldValue);
            } else {
                characterCountLabel.setText(newValue.length() + "/300");
            }
        });

        // Update rating value label when slider changes
        ratingSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            ratingValueLabel.setText(String.valueOf(newValue.intValue()));
        });

        // Show dialog and process the result
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            int rating = (int) ratingSlider.getValue();
            String reviewText = reviewTextArea.getText().trim();

            if (reviewText.isEmpty()) {
                // Submit the review
                submitReview(rating, "");
                GUIController.creatingNewReview = false;
            } else {
                // Submit the review
                submitReview(rating, reviewText);
                GUIController.creatingNewReview = false;
            }
        }
    }

    //Submit a new review to the system
    private void submitReview( int rating, String reviewText) {
        api.addReview(CurrentUser.getInstance(), currentCourse, reviewText, rating);

        loadReviews(currentCourse);

        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Review Submitted");
        alert.setContentText("Your review has been submitted successfully.");
        alert.showAndWait();
        filterCourses(searchField.getText());
        currentCourse = api.getCourse(currentCourse.getCourse_id());
        loadCourseDetails(currentCourse);
        loadCourseList();
        filterCourses(searchField.getText());
    }

    //Update the review count label
    private void updateReviewCountLabel(int count) {
        reviewCountLabel.setText("(" + count + ")");
    }

    //Load the lecturer's note
    private void loadLecturersNote() {
        lecturersNoteTextArea.setText(currentCourse.getLecturersNote());

        lecturersNoteTextArea.setWrapText(true);
        lecturersNoteTextArea.setEditable(false);
        lecturersNoteTextArea.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");
    }


    private void editLecturersNote() {
        loadLecturersNote();
        lecturersNoteTextArea.setEditable(true);
        lecturersNoteTextArea.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");
      }

    //Show dialog to rate a professor

    @FXML
    private void showRateProfessorDialog(MouseEvent event) throws IOException {
        if (event.getSource() == profRatingLabel) {
            if (event.getButton() == MouseButton.PRIMARY){
                GUIController.creatingNewReview = true;

                FXMLLoader dialogLoader = new FXMLLoader();
                dialogLoader.setController(GUIController.getInstance()); // Assign the singleton instance of GUIController as the controller
                dialogLoader.setLocation(getClass().getResource("/com/sen3006/coursewise/client/RateProfessorDialog.fxml"));
                DialogPane dialogPane = dialogLoader.load();
                // Create the dialog
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(dialogPane);
                dialog.setTitle("Rate Professor " + profNameLabel.getText());
                dialog.initModality(Modality.APPLICATION_MODAL);
                // Get the dialog components
                Label studentIdLabel = (Label) dialogPane.lookup("#studentIdLabel");
                Slider ratingSlider = (Slider) dialogPane.lookup("#ratingSlider");
                Label ratingValueLabel = (Label) dialogPane.lookup("#ratingValueLabel");

                //Get the student ID from the current user session and set it to the label
                studentIdLabel.setText(String.valueOf(CurrentUser.getInstance().getId()));

                //Update rating value label when slider changes
                ratingSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                    ratingValueLabel.setText(String.valueOf(newValue.intValue()));
                });
                // Show dialog and process the result
                Optional<ButtonType> result = dialog.showAndWait();
                String courseCode = currentCourse.getCourse_id();
                String sectionCode = String.valueOf(currentSection.getSection_id());

                int professorId = 0;

                for (Section section : api.getSections(courseCode, selectedSemester)) {
                    if (section.getCourse().getCourse_id().equals(courseCode) && (String.valueOf(section.getSection_id())).equals(sectionCode)) {
                        professorId = section.getProfessor().getProf_id();
                        break;
                    }
                }
                if (result.isPresent() && result.get() == ButtonType.OK) {

                    int rating = (int) ratingSlider.getValue();
                    updateProfessorRating(professorId, rating);

                    // Submit the review
                    submitRating(professorId, rating);

                }
            }
        }

    }

    //Submit a new rating for a professor
    private void submitRating(int professorId, int rating) {
        System.out.println("Rating submitted: " + rating);
        profRatingLabel.setText(api.getProfessor(professorId).getAvgRating() + "/10");
        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Rating Submitted");
        alert.setContentText("Your rating of " + profNameLabel.getText() + " has been submitted successfully.");
        alert.showAndWait();
    }

    private boolean updateProfessorRating(int profId, int rating) {
        Professor professor = api.getProfessor(profId);
        CurrentUser currentUserId = CurrentUser.getInstance();

        // Update the professor rating label
        profRatingLabel.setText(professor.getAvgRating() + "/10");
        return api.addRating(currentUserId, professor, rating);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Course) {
            Course course = (Course) o;
            if (course.equals(currentCourse)) {
                courseTitleLabel.setText(course.getCourse_name());
                courseRatingLabel.setText(course.getAvgRating() + "/10");
                courseCodeLabel.setText(course.getCourse_id());
                System.out.println("Course updated: " + course.getCourse_name());
            }
        }
        if (o instanceof Review) {
            Review review = (Review) o;
            if (review.getCourse().getCourse_id().equals(currentCourse.getCourse_id())) {
                loadReviews(currentCourse);
                loadCourseDetails(currentCourse);
                System.out.println("Review updated: " + review.getCourse().getCourse_id());
            }
        }
        if (o instanceof Professor) {
            Professor professor = (Professor) o;
            if (professor.getProf_id() == currentSection.getProfessor().getProf_id()) {
                profRatingLabel.setText(professor.getAvgRating() + "/10");
                System.out.println("Professor updated: " + professor.getProf_name());
                System.out.println("Rating updated: " + professor.getAvgRating());
            }
        }
    }
}
