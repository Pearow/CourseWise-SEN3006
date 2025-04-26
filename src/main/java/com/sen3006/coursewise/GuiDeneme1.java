package com.sen3006.coursewise;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
    @FXML private Button addReviewButton;
    @FXML private VBox sectionsContainer;

    private ToggleGroup sectionGroup;
    private String currentCourseCode;
    private String currentSectionCode;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize search functionality
        setupSearch();

        // Load course list from API or database
        loadCourseList();

        // Create the section toggle group
        sectionGroup = new ToggleGroup();

        // Set up the search text field listener to filter courses
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCourses(newValue);
        });
    }

    //Sets up search functionality
    private void setupSearch() {
        // TODO: Implement your search functionality here
        // searchField.textProperty().addListener((obs, oldVal, newVal) -> {
        //     yourSearchFunction(newVal);
        // });
    }

    /**
     * Loads course list from API or database
     * Replace with your API or database call
     */
    private void loadCourseList() {
        courseListContainer.getChildren().clear();

        // TODO: Replace with your API call to get courses
        // List<Course> courses = yourApiService.getCourses();
        // for (Course course : courses) {
        //     addCourseToList(course);
        // }

        // Example: Add some sample courses for now
        addSampleCoursesToList();
    }

    //This method should be replaced with your actual API calls
    private void addSampleCoursesToList() {
        addCourseItemToList("SEN3006", "9/10");
        addCourseItemToList("CSE2025", "8/10");
        addCourseItemToList("MBG1201", "7/10");
        addCourseItemToList("ECON101", "8/10");
        addCourseItemToList("PHYS101", "7/10");
        addCourseItemToList("MATH241", "9/10");
    }

    /**
     * Add a course item to the list
     * Replace parameters with your Course class when integrating
     */
    private void addCourseItemToList(String courseCode, String rating) {
        HBox courseItem = new HBox();
        courseItem.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        courseItem.setSpacing(10.0);
        courseItem.setPadding(new Insets(5));

        // Style for a default course item
        courseItem.setStyle("-fx-background-color: white; -fx-background-radius: 4; -fx-border-color: #e0e0e0; -fx-border-radius: 4;");

        Label codeLabel = new Label(courseCode);
        codeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #424242;");

        Label ratingLabel = new Label(rating);
        ratingLabel.setStyle("-fx-border-color: #757575; -fx-border-radius: 10; -fx-background-color: white; -fx-background-radius: 10; -fx-text-fill: #757575;");
        ratingLabel.setPadding(new Insets(2, 5, 2, 5));

        courseItem.getChildren().addAll(codeLabel, ratingLabel);

        // Add click event to load course details
        courseItem.setOnMouseClicked(event -> loadCourseDetails(courseCode, rating));
        courseItem.setStyle(courseItem.getStyle() + "; -fx-cursor: hand;");

        // Add hover effect
        courseItem.setOnMouseEntered(event ->
                courseItem.setStyle(courseItem.getStyle().replace("-fx-background-color: white;", "-fx-background-color: #f5f5f5;")));

        courseItem.setOnMouseExited(event ->
                courseItem.setStyle(courseItem.getStyle().replace("-fx-background-color: #f5f5f5;", "-fx-background-color: white;")));

        courseListContainer.getChildren().add(courseItem);

        // Add separator except for the last item
        if (courseListContainer.getChildren().size() > 0 &&
                !courseCode.equals("MATH241")) { // This should be dynamically checked in your actual implementation
            Separator separator = new Separator();
            separator.setStyle("-fx-background-color: #e0e0e0;");
            courseListContainer.getChildren().add(separator);
        }
    }

    //Filter courses based on search text
    private void filterCourses(String searchText) {
        // TODO: Replace with your filtering logic
        // You could call your API again with a filter parameter or filter the existing list

        // Example implementation:
        // clearCourseList();
        // List<Course> filteredCourses = yourApiService.searchCourses(searchText);
        // for (Course course : filteredCourses) {
        //     addCourseToList(course);
        // }
    }

    //Load course details when a course is selected
    private void loadCourseDetails(String courseCode, String rating) {
        // Save current course code
        this.currentCourseCode = courseCode;

        // TODO: Replace with your API call
        // Course course = yourApiService.getCourseDetails(courseCode);
        // updateCourseDetailsUI(course);

        // For now, use example data
        courseCodeLabel.setText(courseCode);
        courseRatingLabel.setText(rating); // This should come from your API
        courseTitleLabel.setText("Software Engineering"); // From API
        courseTypeLabel.setText("Lecture"); // From API

        // Load available sections
        loadAvailableSections(courseCode);

        // If sections are available, load the first section by default
        if (!sectionsContainer.getChildren().isEmpty()) {
            RadioButton firstSection = (RadioButton) sectionsContainer.getChildren().get(0);
            firstSection.setSelected(true);
            loadSectionDetails(firstSection.getText());
        }
    }

    //Load available sections for a course
    private void loadAvailableSections(String courseCode) {
        sectionsContainer.getChildren().clear();
        sectionGroup = new ToggleGroup();

        // TODO: Replace with your API call
        // List<Section> sections = yourApiService.getSections(courseCode);
        // for (Section section : sections) {
        //     addSectionToList(section);
        // }

        // Add sections based on course code
        if (courseCode.equals("SEN3006")) {
            addSectionToList(courseCode + "(1)");
            addSectionToList(courseCode + "(2)");
            addSectionToList(courseCode + "(3)");
        } else if (courseCode.equals("CSE2025")) {
            addSectionToList(courseCode + "(1)");
            addSectionToList(courseCode + "(2)");
        } else if (courseCode.equals("MBG1201")) {
            addSectionToList(courseCode + "(1)");
            addSectionToList(courseCode + "(2)");
            addSectionToList(courseCode + "(3)");
            addSectionToList(courseCode + "(4)");
        } else {
            // Default sections for other courses
            addSectionToList(courseCode + "(1)");
            addSectionToList(courseCode + "(2)");
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
        courseCodeLabel.setText(sectionCode);

        // TODO: Replace with your API call
        // Section section = yourApiService.getSectionDetails(sectionCode);
        // updateSectionDetailsUI(section);

        // Example implementation
        if (sectionCode.endsWith("(1)")) {
            weekdayLabel.setText("Tuesday");
            durationLabel.setText("10:30 - 13:20");
            roomLabel.setText("G103");
            profNameLabel.setText("Dr. Smith");
            profRatingLabel.setText("9/10");
            campusLabel.setText("South Campus");
        } else if (sectionCode.endsWith("(2)")) {
            weekdayLabel.setText("Thursday");
            durationLabel.setText(" 14:00 - 16:50");
            roomLabel.setText("B204");
            profNameLabel.setText("Dr. Johnson");
            profRatingLabel.setText("8/10");
            campusLabel.setText("North Campus");
        } else if (sectionCode.endsWith("(3)")) {
            weekdayLabel.setText("Monday");
            durationLabel.setText("09:00 - 11:50");
            roomLabel.setText("C305");
            profNameLabel.setText("Dr. Williams");
            profRatingLabel.setText("7/10");
            campusLabel.setText("Central Campus");
        } else {
            // Default or random values for sections 4-6
            weekdayLabel.setText("Friday");
            durationLabel.setText("13:00 - 15:50");
            roomLabel.setText("D" + sectionCode.charAt(sectionCode.length()-2) + "01");
            profNameLabel.setText("Dr. Brown");
            profRatingLabel.setText("8/10");
            campusLabel.setText("East Campus");
        }

        // Load reviews for this section
        loadReviews(sectionCode);
    }

    //Load reviews for a specific section
    //TODO: Load reviews of a course, not a section
    private void loadReviews(String sectionCode) {
        reviewsContainer.getChildren().clear();

        // TODO: Replace with your API call
        // List<Review> reviews = yourApiService.getReviews(sectionCode);
        // updateReviewCountLabel(reviews.size());
        // for (Review review : reviews) {
        //     addReviewToContainer(review);
        // }

        // Example implementation with sample data

        if (sectionCode.endsWith("(1)")) {
            updateReviewCountLabel(36);
            addReviewToContainer("2200870", "9/10", "Highly educational course, the project assignment helped me understand the topics", "#e8f5e9", "#2e7d32");
            addReviewToContainer("2190456", "8/10", "The professor explains concepts clearly, but assignments can be challenging.", "#e3f2fd", "#1565c0");
            addReviewToContainer("2205123", "10/10", "Best course I've taken so far! The professor is very helpful and the material is relevant to industry standards.", "#f3e5f5", "#6a1b9a");
        } else if (sectionCode.endsWith("(2)")) {
            updateReviewCountLabel(24);
            addReviewToContainer("2204567", "7/10", "Lectures can be a bit fast-paced, but overall good content.", "#e3f2fd", "#1565c0");
            addReviewToContainer("2198765", "9/10", "Dr. Johnson is very knowledgeable and explains complex topics well.", "#e8f5e9", "#2e7d32");
        } else {
            updateReviewCountLabel(15);
            addReviewToContainer("2203456", "8/10", "Interesting course material but could use more practical examples.", "#e3f2fd", "#1565c0");
        }
    }

    //Update the review count label
    private void updateReviewCountLabel() {
        String count;
        reviewCountLabel.setText("(" + "count" + ")");
    }

    //Add a review to the reviews container
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
        if (reviewsContainer.getChildren().size() > 0 &&
                reviewsContainer.getChildren().indexOf(reviewBox) < reviewsContainer.getChildren().size() - 1) {
            Separator separator = new Separator();
            separator.setStyle("-fx-background-color: #e0e0e0;");
            reviewsContainer.getChildren().add(separator);
        }
    }

    //Show dialog to add a new review
    @FXML
    private void showAddReviewDialog() {
        try {
            // Load the dialog FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/sen3006/coursewise/ReviewDialog.fxml"));
            DialogPane dialogPane = loader.load();

            // Create the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add Review");
            dialog.initModality(Modality.APPLICATION_MODAL);

            // Get the dialog components
            Label studentIdLabel = (Label) dialogPane.lookup("#studentIdLabel");
            Slider ratingSlider = (Slider) dialogPane.lookup("#ratingSlider");
            Label ratingValueLabel = (Label) dialogPane.lookup("#ratingValueLabel");
            TextArea reviewTextArea = (TextArea) dialogPane.lookup("#reviewTextArea");
            Label characterCountLabel = (Label) dialogPane.lookup("#characterCountLabel");

            studentIdLabel.setText("2200870"); // Replace with actual student ID from the session

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
                    // Show error alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Input");
                    alert.setContentText("Please fill in all fields.");
                    alert.showAndWait();
                } else {
                    // Submit the review
                    String studentId = studentIdLabel.getText(); // Replace with actual student ID from the session
                    submitReview(studentId, rating, reviewText);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error", "Could not open review dialog.", e.getMessage());
        }
    }



    //Update the course rating after a new review is submitted
    private void updateCourseRating(String sectionCode) {
        // TODO: Call your API to get the updated rating
        // double newRating = yourApiService.getCourseRating(sectionCode);
        // courseRatingLabel.setText(String.format("%.1f/10", newRating));
    }

    //Show error alert
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //Sets up the add review button with "+" symbol
    private void setupAddReviewButton() {
        Button addButton = new Button("+");
        addButton.getStyleClass().add("add-button");
        addButton.setOnAction(event -> showAddReviewDialog());

        // Add the button to your layout (toolbar, pane, etc.)
        // Example: toolbarBox.getChildren().add(addButton);
    }

    //Submit a new review to the system
    private void submitReview(String studentId, int rating, String reviewText) {
        // TODO: Call your API to submit the review
        // boolean success = yourApiService.submitReview(currentSectionCode, studentId, rating, reviewText);

        String ratingStr = rating + "/10";

        // Choose color based on rating
        String bgColor;
        String textColor;

        if (rating >= 9) {
            bgColor = "#e8f5e9";
            textColor = "#2e7d32";
        } else if (rating >= 7) {
            bgColor = "#e3f2fd";
            textColor = "#1565c0";
        } else if (rating >= 5) {
            bgColor = "#fff3e0";
            textColor = "#e65100";
        } else {
            bgColor = "#ffebee";
            textColor = "#c62828";
        }

        // Add review to UI
        addReviewToContainer(studentId, ratingStr, reviewText, bgColor, textColor);

        // Update review count
        String countText = reviewCountLabel.getText();
        int currentCount = 0;
        if (countText != null && countText.length() > 2) {
            currentCount = Integer.parseInt(countText.substring(1, countText.length() - 1));
        }
        updateReviewCountLabel(currentCount + 1);

        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Review Submitted");
        alert.setContentText("Your review has been submitted successfully.");
        alert.showAndWait();
    }


    // Add the review to UI container
    private void addReviewToContainer(String rating, String reviewText, String bgColor, String textColor) {
        // Create review pane
        VBox reviewBox = new VBox(5);
        reviewBox.setPadding(new Insets(10));
        reviewBox.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 5;");

        // Rating
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label ratingLabel = new Label("Rating: " + rating);
        ratingLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + textColor + ";");

        headerBox.getChildren().add(ratingLabel);

        // Review text
        Label reviewLabel = new Label(reviewText);
        reviewLabel.setWrapText(true);
        reviewLabel.setStyle("-fx-text-fill: " + textColor + ";");

        // Add all components to review box
        reviewBox.getChildren().addAll(headerBox, reviewLabel);

        // Add to review container
        // Assuming you have a container for reviews such as VBox reviewsContainer
        reviewsContainer.getChildren().add(reviewBox);
    }

    //Update the review count label
    private void updateReviewCountLabel(int count) {
        reviewCountLabel.setText("(" + count + ")");
    }
}
