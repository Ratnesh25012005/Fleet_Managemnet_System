package com.example.car_rental_sys.ui_components;

import com.example.car_rental_sys.StatusContainer;
import com.example.car_rental_sys.ToolsLib.FXTools;
import com.example.car_rental_sys.ToolsLib.ImageTools;
import com.example.car_rental_sys.controllers.ViewCommentsPageController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Authentic customer review card for carDetailsPage.fxml & ViewCommentsPage.fxml.
 */
public class CommentCard extends Pane {

    private String commentID;
    private int cardHeight = 90;
    private int cardWidth = 770;
    private String userName = "Alex Johnson";
    private String commentTime = "2026-10-28";
    private String commentText = "Phenomenal vehicle. Immaculate condition, exceptional handling, and seamless booking experience.";
    private int likes = 12;
    private int stars = 5;

    private Button likeBtn, starBtn, replyBtn;
    private boolean liked = false;
    private boolean starred = false;
    private VBox vBoxRightSide;

    public CommentCard(String commentID) {
        super();
        this.commentID = commentID;
        initialize();
    }

    public CommentCard(String userName, String commentText, String commentTime, int likes, int stars) {
        super();
        this.userName = userName;
        this.commentText = commentText;
        this.commentTime = commentTime;
        this.likes = likes;
        this.stars = stars;
        this.commentID = "1";
        initialize();
    }

    private void initialize() {
        setSize();
        setProfileImage();
        initRightSide();
        initEvent();
    }

    private void setSize() {
        this.setPrefHeight(cardHeight);
        this.setPrefWidth(cardWidth);
    }

    private void setProfileImage() {
        Image image = ImageTools.getImageObjFromPath("/com/example/car_rental_sys/image/avatar/avatar_male.png");
        ImageView imageView = new ImageView();
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        if (image != null) imageView.setImage(image);

        imageView.setLayoutX(20);
        imageView.setLayoutY(10);

        this.getChildren().add(imageView);
        ImageTools.setImageShapeToCircle(imageView);
    }

    private HBox getUserNameAndCommentTime() {
        HBox hBox = new HBox(12);
        Label userNameLabel = new Label(userName);
        userNameLabel.setStyle("-fx-text-fill: #e5e6eb; -fx-font-weight: bold; -fx-font-size: 13px;");

        Label timeLabel = new Label(commentTime);
        timeLabel.setStyle("-fx-text-fill: #86909c; -fx-font-size: 12px;");

        hBox.getChildren().addAll(userNameLabel, timeLabel);
        return hBox;
    }

    private Label getCommentContent() {
        Label content = new Label(commentText);
        content.setPrefHeight(20);
        content.setFont(new Font(14));
        content.setStyle("-fx-text-fill: #c9cdd4;");
        return content;
    }

    private HBox getToolbar() {
        HBox hBox = new HBox(16);

        likeBtn = new Button(String.valueOf(likes));
        setButtonIcon(likeBtn, "/com/example/car_rental_sys/image/UI/commentLike.png");
        likeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #86909c; -fx-cursor: hand;");

        starBtn = new Button(String.valueOf(stars));
        setButtonIcon(starBtn, "/com/example/car_rental_sys/image/UI/commentStar.png");
        starBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #86909c; -fx-cursor: hand;");

        replyBtn = new Button("Reply");
        setButtonIcon(replyBtn, "/com/example/car_rental_sys/image/UI/commentReply.png");
        replyBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #86909c; -fx-cursor: hand;");

        hBox.getChildren().addAll(likeBtn, starBtn, replyBtn);
        return hBox;
    }

    private void setButtonIcon(Button btn, String resourcePath) {
        Image img = ImageTools.getImageObjFromPath(resourcePath);
        if (img != null) {
            ImageView iv = new ImageView(img);
            iv.setFitWidth(16);
            iv.setFitHeight(16);
            btn.setGraphic(iv);
        }
    }

    private void initRightSide() {
        vBoxRightSide = new VBox(6);
        vBoxRightSide.setLayoutX(70);
        vBoxRightSide.setLayoutY(10);
        vBoxRightSide.getChildren().addAll(getUserNameAndCommentTime(), getCommentContent(), getToolbar());

        this.getChildren().add(vBoxRightSide);
        this.setStyle("-fx-border-color: #333333; -fx-border-style: solid none none none; -fx-border-width: 1px;");
    }

    private void initEvent() {
        likeBtn.setOnAction(event -> {
            if (liked) return;
            likes++;
            likeBtn.setText(String.valueOf(likes));
            setButtonIcon(likeBtn, "/com/example/car_rental_sys/image/UI/commentLiked.png");
            likeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #165dff; -fx-cursor: hand;");
            liked = true;
        });

        starBtn.setOnAction(event -> {
            if (starred) return;
            stars++;
            starBtn.setText(String.valueOf(stars));
            setButtonIcon(starBtn, "/com/example/car_rental_sys/image/UI/commentStarred.png");
            starBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffb400; -fx-cursor: hand;");
            starred = true;
        });

        replyBtn.setOnAction(event -> {
            if (!StatusContainer.ifInCommentPage) {
                FXTools.changeScene("ViewCommentsPage.fxml");
            } else if (ViewCommentsPageController.instance != null && ViewCommentsPageController.instance.textArea != null) {
                ViewCommentsPageController.instance.textArea.setText("@" + userName + ": ");
            }
        });
    }
}
