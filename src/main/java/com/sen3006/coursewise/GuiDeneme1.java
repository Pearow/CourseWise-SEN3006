package com.sen3006.coursewise;

import com.sen3006.coursewise.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.stage.Modality;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GuiDeneme1 implements Initializable {

    @FXML private TextField searchField;
    @FXML private VBox courseListContainer;

    @FXML private Label courseCodeLabel;
    @FXML private Label courseRatingLabel;
    @FXML private Label courseTitleLabel;
    @FXML private Label courseTypeLabel;
    @FXML private Label profNameLabel;
    @FXML private Label profRatingLabel;
    @FXML private Label weekdayLabel;
    @FXML private Label durationLabel;
    @FXML private Label campusLabel;
    @FXML private Label roomLabel;
    @FXML private Label reviewCountLabel;

    @FXML private VBox reviewsContainer;
    @FXML private VBox sectionsContainer;

    @FXML private TextArea lecturersNoteTextArea;

    private ToggleGroup sectionGroup;
    private Course currentCourse;
    private static boolean creatingNewReview = false;
    private String currentSectionCode;

    API api = API.getInstance();
    CurrentUser currentUser = CurrentUser.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        if (!creatingNewReview) {
            // Load course list from API or database
            loadCourseList();
            // Create the section toggle group
            sectionGroup = new ToggleGroup();

            // Set up the search text field listener to filter courses
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterCourses(newValue);
            });

            //Select the first course by default
            if (!courseListContainer.getChildren().isEmpty()) {
                currentCourse = api.getCourses()[0];
                loadCourseDetails(currentCourse, String.valueOf(currentCourse.getAvgRating()));
            }
        }
    }

    //Loads course list from API or database
    private void loadCourseList() {
        courseListContainer.getChildren().clear();

        Course[] courses = api.getCourses(); //this method is broken for now and returns every course in the database
        for (Course course : courses) {
            addCourseItemToList(course, String.valueOf(course.getAvgRating()));
        }
    }

    //Add a course item to the list
    private void addCourseItemToList(Course course, String rating) {
        HBox courseItem = new HBox();
        courseItem.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        courseItem.setSpacing(10.0);
        courseItem.setPadding(new Insets(5));

        // Style for a default course item
        courseItem.setStyle("-fx-background-color: white; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");

        Label codeLabel = new Label(course.getCourse_id());
        codeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #424242;");

        Label ratingLabel = new Label(rating + "/10");
        ratingLabel.setStyle("-fx-border-color: #757575; -fx-border-radius: 10; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: #757575;");
        ratingLabel.setPadding(new Insets(2, 5, 2, 5));
        ratingLabel.setMinWidth(40);

        courseItem.getChildren().addAll(codeLabel, ratingLabel);

        // Add click event to load course details
        courseItem.setOnMouseClicked(event -> loadCourseDetails(course, rating));
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
        if (searchText.length() < 3) {
            loadCourseList(); // Show all courses if less than 3 characters
            return;
        }

        Course[] courses = api.getCourses(); // Get courses from API

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
    private void loadCourseDetails(Course course, String rating) {
        // Save current course code
        currentCourse = course;
        if (course != null) {
            // Update course details UI
            courseTitleLabel.setText(course.getCourse_name());
            courseRatingLabel.setText(rating + "/10");
            courseCodeLabel.setText(course.getCourse_id());
            courseTypeLabel.setText(course.getType().toString());
        }

        // Load available sections
        loadAvailableSections(course);
        loadLecturersNote();

        // If sections are available, load the first section by default
        if (!sectionsContainer.getChildren().isEmpty()) {
            RadioButton firstSection = (RadioButton) sectionsContainer.getChildren().get(0);
            firstSection.setSelected(true);
            loadSectionDetails(firstSection.getText());
        }
    }

    //Load available sections for a course
    private void loadAvailableSections(Course course) {
        sectionsContainer.getChildren().clear();
        sectionGroup = new ToggleGroup();

        Section[] sections = api.getSections(course.getCourse_id()); // This should return the list of sections for the course
        for (Section section : sections) {
            addSectionToList(String.valueOf(section.getSection_id()));
        }

        // Select first section by default
        if (!sectionsContainer.getChildren().isEmpty()) {
            RadioButton firstSection = (RadioButton) sectionsContainer.getChildren().get(0);
            firstSection.setSelected(true);
            loadSectionDetails(firstSection.getText());
        }
    }

    //Add a section to the sections list
    private void addSectionToList(String sectionCode) {
        RadioButton radioButton = new RadioButton(sectionCode);
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
        radioButton.setOnAction(event -> loadSectionDetails(sectionCode));

        sectionsContainer.getChildren().add(radioButton);
    }

    //Load section details when a section is selected
    private void loadSectionDetails(String sectionCode) {
        this.currentSectionCode = sectionCode;
        courseCodeLabel.setText(currentCourse.getCourse_id());

        for (Section s : api.getSections(currentCourse.getCourse_id())) {
            if (s.getSection_id() == Integer.parseInt(sectionCode)) {
                weekdayLabel.setText(s.getSection_day().toString());
                durationLabel.setText(s.getStart_time().toString() + " - " + s.getEnd_time().toString());
                roomLabel.setText(s.getClassroom().getClass_id());
                profNameLabel.setText(s.getProfessor().getProf_name() + " " + s.getProfessor().getSurname());
                profRatingLabel.setText(s.getProfessor().getAvgRating() + "/10");
                campusLabel.setText(String.valueOf(s.getClassroom().getCampus()));
            }
        }

        // Load reviews for this section
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
        String rating = String.valueOf(review.getRating()) + "/10";
        String comment = review.getComment();

        String bgColor = "";
        String textColor = "";
        
        if(review.getRating() == 10){
            bgColor = "#f3e5f5"; 
            textColor = "#6a1b9a";
        }
        else if(review.getRating() == 9){
            bgColor = "#e8f5e9";
            textColor = "#2e7d32";
        }
        else if(review.getRating() == 8){
            bgColor = "#e3f2fd";
            textColor = "#1565c0";
        } else if (review.getRating() == 7) {
            bgColor = "#fff3e0";
            textColor = "#e65100";
        } else if (review.getRating() == 6) {
            bgColor = "#ffebee";
            textColor = "#c62828";
        } else if (review.getRating() == 5) {
            bgColor = "#fce4ec";
            textColor = "#ad1457";
        } else if (review.getRating() == 4) {
            bgColor = "#f3e5f5";
            textColor = "#6a1b9a";
        } else if (review.getRating() == 3) {
            bgColor = "#ede7f6";
            textColor = "#4527a0";
        } else if (review.getRating() == 2) {
            bgColor = "#e8eaf6";
            textColor = "#283593";
        } else if (review.getRating() == 1) {
            bgColor = "#e3f2fd";
            textColor = "#1565c0";
        }

        VBox reviewBox = new VBox(5);
        reviewBox.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 4; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 2, 0, 0, 1);");
        reviewBox.setPadding(new Insets(5));

        HBox reviewHeader = new HBox(10);
        reviewHeader.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label reviewUser = new Label("→ " + reviewer + ",");
        reviewUser.setStyle("-fx-text-fill:" + textColor + ";");

        Label reviewRating = new Label(rating);
        reviewRating.setStyle("-fx-border-color:" + textColor + "; -fx-border-radius: 10; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: " + textColor + ";");
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

    //Add a review to the reviews container
    //TODO: Update after Review model is implemented
    private void addReviewToContainer(String studentId, String rating, String reviewText, String bgColor, String textColor) {
        VBox reviewBox = new VBox(5);
        reviewBox.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 4; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 2, 0, 0, 1);");
        reviewBox.setPadding(new Insets(5));

        HBox reviewHeader = new HBox(10);
        reviewHeader.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label reviewUser = new Label("→ " + studentId + ",");
        reviewUser.setStyle("-fx-text-fill: " + textColor + ";");

        Label reviewRating = new Label(rating);
        reviewRating.setStyle("-fx-border-color: " + textColor + "; -fx-border-radius: 10; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: " + textColor + ";");
        reviewRating.setPadding(new Insets(2, 5, 2, 5));

        reviewHeader.getChildren().addAll(reviewUser, reviewRating);

        Label reviewContent = new Label(reviewText);
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
    //TODO: Check if the user has selected a section before adding a review
    //TODO: Check if the current user is a student
    @FXML
    private void showAddReviewDialog(ActionEvent event) throws IOException {
        creatingNewReview = true;
        // Load the dialog FXML
        FXMLLoader dialogLoader = new FXMLLoader(getClass().getResource("/com/sen3006/coursewise/ReviewDialog.fxml"));
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
        studentIdLabel.setText(String.valueOf(currentUser.getId()));

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
                String studentId = String.valueOf(currentUser.getId());
                submitReview(studentId, rating, "");
                creatingNewReview = false;
            } else {
                // Submit the review
                String studentId = String.valueOf(currentUser.getId());
                submitReview(studentId, rating, reviewText);
                creatingNewReview = false;
            }
        }
    }

    //Submit a new review to the system
    private void submitReview(String studentId, int rating, String reviewText) {
        api.addReview(currentUser, currentCourse, reviewText, rating);

        loadReviews(currentCourse);
        loadCourseDetails(currentCourse, String.valueOf(currentCourse.getAvgRating()));

        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Review Submitted");
        alert.setContentText("Your review has been submitted successfully.");
        alert.showAndWait();
    }

    //Update the review count label
    private void updateReviewCountLabel(int count) {
        reviewCountLabel.setText("(" + count + ")");
    }

    //Load the lecturer's note
    private void loadLecturersNote() {

        //Create a some placeholder text for the lecturer's note
        //TODO: Replace with your API call

        // Course currentCourse = api.getCourse(currentCourseCode)
        // lecturersNoteTextArea.setText(currentCourse.getNote())
        // also check if currentCourseCode.getNote() == null, if so, setText like the else statement below
        // TODO: Delete placeholders
        if (currentCourse.getCourse_id().equals("SEN3006")) {
            lecturersNoteTextArea.setText("This course is designed to provide students with a comprehensive understanding of software engineering principles and practices. Students will learn about software development methodologies, project management, and quality assurance.");
        } else if (currentCourse.getCourse_id().equals("CSE2025")) {
            lecturersNoteTextArea.setText("This course covers the fundamentals of computer science, including algorithms, data structures, and programming languages. Students will gain hands-on experience through practical assignments.");
        } else if (currentCourse.getCourse_id().equals("MBG1201")) {
            lecturersNoteTextArea.setText("This course introduces students to the principles of molecular biology and genetics. Students will learn about DNA structure, replication, and gene expression.");
        } else if (currentCourse.getCourse_id().equals("ECON101")) {
            lecturersNoteTextArea.setText("This course provides an introduction to microeconomics and macroeconomics. Students will learn about supply and demand, market structures, and economic indicators.");
        } else if (currentCourse.getCourse_id().equals("PHYS101")) {
            lecturersNoteTextArea.setText("This course covers the basic principles of physics, including mechanics, thermodynamics, and electromagnetism. Students will engage in laboratory experiments to reinforce theoretical concepts.");
        }else {
            lecturersNoteTextArea.setText("No notes available for this course.");
        }

        lecturersNoteTextArea.setWrapText(true);
        lecturersNoteTextArea.setEditable(false);
        lecturersNoteTextArea.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");
    }

    //
    //TODO: Check if the user is a Professor
    private void editLecturersNote() {
        loadLecturersNote();
        lecturersNoteTextArea.setEditable(true);
        lecturersNoteTextArea.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");
//        lecturersNoteTextArea.setOnKeyPressed(event -> {
//            if (event.getCode().toString().equals("ENTER")) {
//                // Save the note
//                String note = lecturersNoteTextArea.getText();
                  //api.getCourse(curretnCourseCode).setNote(note)
//            }
//            if (event.getCode().toString().equals("ESCAPE")) {
//                // Cancel editing
//                loadLecturersNote();
//            }
//        });
    }

    //Show dialog to rate a professor
    //TODO: Check if the user has already rated this professor
    //TODO: Check if the current user is a student
    @FXML
    private void showRateProfessorDialog(MouseEvent event) throws IOException {
        if (event.getSource() == profRatingLabel) {
            if (event.getButton() == MouseButton.PRIMARY){
                creatingNewReview = true;
                // Load the dialog FXML
                FXMLLoader dialogLoader = new FXMLLoader(getClass().getResource("/com/sen3006/coursewise/RateProfessorDialog.fxml"));
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
                studentIdLabel.setText(String.valueOf(currentUser.getId()));

                //Update rating value label when slider changes
                ratingSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                    ratingValueLabel.setText(String.valueOf(newValue.intValue()));
                });
                // Show dialog and process the result
                Optional<ButtonType> result = dialog.showAndWait();
                String courseCode = courseCodeLabel.getText();
                String sectionCode = currentSectionCode;

                int professorId = 0;

                for (Section section : api.getSections(courseCode)) {
                    if (section.getCourse().getCourse_id().equals(courseCode) && (String.valueOf(section.getSection_id())).equals(sectionCode)) {
                        professorId = section.getProfessor().getProf_id();
                        break;
                    }
                }
                if (result.isPresent() && result.get() == ButtonType.OK) {

                    int rating = (int) ratingSlider.getValue();
                    updateProfessorRating(professorId, rating);

                    // Submit the review
                    String studentId = studentIdLabel.getText(); // Replace with actual student ID from the session
                    submitRating(studentId, rating,String.valueOf(professorId)); // Empty review text for professor rating
                    System.out.println("Rating submitted: " + rating);
                    System.out.println(sectionCode);
                    profRatingLabel.setText(rating + "/10");
                }



                System.out.println("Clicked");
            }
        }

    }

    //Submit a new rating for a professor
    //TODO: Call your API to submit the rating
    private void submitRating(String studentId, int rating, String profId) {

        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Rating Submitted");
        alert.setContentText("Your rating of " + profNameLabel.getText() + " has been submitted successfully.");
        alert.showAndWait();
    }

    private boolean updateProfessorRating(int profId, int rating) {
        Professor professor = api.getProfessor(profId);
        return professor.addRating(rating);

        // Update the professor rating label
    }
}
