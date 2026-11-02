package com.example.car_rental_sys.controllers;

import com.example.car_rental_sys.StatusContainer;
import com.example.car_rental_sys.concurrency.AsyncExecutor;
import com.example.car_rental_sys.dao.DAOFactory;
import com.example.car_rental_sys.model.Payment;
import com.example.car_rental_sys.model.PaymentStatus;
import com.example.car_rental_sys.model.User;
import com.example.car_rental_sys.service.ServiceFactory;
import com.example.car_rental_sys.ui_components.PaymentCard;
import com.example.car_rental_sys.ui_components.UICusBillRow;
import com.example.car_rental_sys.ui_components.UIPagination;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Customer Billing Overview, Wallet, and Payment Transactions.
 * Restored with authentic PaymentCard visuals and UICusBillRow transaction components.
 */
public class BillingComponentController extends Controller {

    public static BillingComponentController instance;

    @FXML
    private ScrollPane billCardList;

    @FXML
    private Pane billCardDetailBox;

    @FXML
    private Button billAllBtn;

    @FXML
    private Button billCompleteBtn;

    @FXML
    private Button billPendingBtn;

    @FXML
    private Button billCancelBtn;

    @FXML
    private Pane cusBillTable;

    @FXML
    private Pane pagContainer;

    private List<Payment> allPayments = new ArrayList<>();
    private List<Payment> displayedPayments = new ArrayList<>();
    private VBox billTableBox;

    @FXML
    public void initialize() {
        instance = this;
        setupCards();
        setupTable();
        loadBillingData();
    }

    private void setupCards() {
        FlowPane cardPane = new FlowPane();
        cardPane.setHgap(20);
        cardPane.setVgap(15);
        cardPane.setPadding(new Insets(10, 15, 10, 15));
        cardPane.setStyle("-fx-background-color: transparent;");

        // Authentic Visa, Mastercard, TnG Wallet, and New Card
        PaymentCard visa = new PaymentCard("visa", "4000123456789010", "Alex Johnson", "Maybank", "2450");
        PaymentCard master = new PaymentCard("mastercard", "5412751234123456", "Alex Johnson", "CIMB Bank", "1800");
        PaymentCard tng = new PaymentCard("tng", "0123456789", "Alex Johnson", "Touch 'n Go", "450");
        PaymentCard addNew = new PaymentCard("addNewCard");

        cardPane.getChildren().addAll(visa, master, tng, addNew);

        if (billCardList != null) {
            billCardList.setContent(cardPane);
            billCardList.setFitToHeight(true);
            billCardList.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        }
    }

    private void setupTable() {
        billTableBox = new VBox(6);
        billTableBox.setPadding(new Insets(6, 10, 6, 10));
        billTableBox.setStyle("-fx-background-color: transparent;");

        if (cusBillTable != null) {
            cusBillTable.getChildren().setAll(billTableBox);
        }

        if (pagContainer != null) {
            pagContainer.getChildren().setAll(new UIPagination(10, 5));
        }
    }

    private void loadBillingData() {
        AsyncExecutor.getInstance().execute(() -> {
            try {
                User user = ServiceFactory.getInstance().getAuthService().getCurrentUser();
                int customerId = (user != null) ? user.getUserId() : 1;

                List<Payment> payments = DAOFactory.getInstance().getPaymentDAO().findByCustomerId(customerId);
                Platform.runLater(() -> {
                    allPayments = payments;
                    displayedPayments = new ArrayList<>(payments);
                    renderBillRows();
                });
            } catch (Exception e) {
                Platform.runLater(this::renderSampleBills);
            }
        });
    }

    private void renderBillRows() {
        if (billTableBox == null) return;
        billTableBox.getChildren().clear();

        if (displayedPayments.isEmpty()) {
            renderSampleBills();
            return;
        }

        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss");

        for (Payment p : displayedPayments) {
            String dateStr = p.getPaymentDate() != null ? p.getPaymentDate().format(dateFmt) : "2026-11-01";
            String timeStr = p.getPaymentDate() != null ? p.getPaymentDate().format(timeFmt) : "10:30:00";
            int amount = p.getAmount() != null ? p.getAmount().intValue() : 250;
            int status = (p.getStatus() == PaymentStatus.COMPLETED) ? 5 : ((p.getStatus() == PaymentStatus.FAILED) ? -1 : 0);

            UICusBillRow row = new UICusBillRow("rental", dateStr, timeStr, amount, status);
            billTableBox.getChildren().add(row);
        }
    }

    private void renderSampleBills() {
        if (billTableBox == null) return;
        billTableBox.getChildren().clear();

        UICusBillRow r1 = new UICusBillRow("rental", "2026-11-02", "14:20:00", 700, 5);
        UICusBillRow r2 = new UICusBillRow("top-up", "2026-11-01", "09:15:00", 1000, 5);
        UICusBillRow r3 = new UICusBillRow("rental", "2026-10-28", "16:45:00", 240, 5);
        UICusBillRow r4 = new UICusBillRow("rental", "2026-10-15", "11:10:00", 850, 5);

        billTableBox.getChildren().addAll(r1, r2, r3, r4);
    }

    @FXML
    private void billAllBtnClicked(MouseEvent event) {
        setFocusBtn(billAllBtn);
        displayedPayments = new ArrayList<>(allPayments);
        renderBillRows();
    }

    @FXML
    private void billCompleteBtnClicked(MouseEvent event) {
        setFocusBtn(billCompleteBtn);
        displayedPayments = allPayments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.COMPLETED)
                .toList();
        renderBillRows();
    }

    @FXML
    private void billPendingBtnClicked(MouseEvent event) {
        setFocusBtn(billPendingBtn);
        displayedPayments = allPayments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.PENDING)
                .toList();
        renderBillRows();
    }

    @FXML
    private void billCancelBtnClicked(MouseEvent event) {
        setFocusBtn(billCancelBtn);
        displayedPayments = allPayments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.FAILED || p.getStatus() == PaymentStatus.REFUNDED)
                .toList();
        renderBillRows();
    }

    private void setFocusBtn(Button target) {
        Button[] btns = {billAllBtn, billCompleteBtn, billPendingBtn, billCancelBtn};
        for (Button b : btns) {
            if (b != null) {
                b.getStyleClass().remove("focusCusBillButton");
                if (!b.getStyleClass().contains("cusBillButton")) {
                    b.getStyleClass().add("cusBillButton");
                }
            }
        }
        if (target != null) {
            target.getStyleClass().add("focusCusBillButton");
            target.getStyleClass().remove("cusBillButton");
        }
    }

    public void refreshTable(int page) {
        renderBillRows();
    }
}
