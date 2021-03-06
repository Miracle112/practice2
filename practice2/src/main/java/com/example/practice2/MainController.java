package com.example.practice2;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.*;


class DBHandler extends Configs{
    Connection dbConnection;
    Statement statement;
    ResultSet resultSet;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }

    public ResultSet querry(String querry) {
        try{
            Statement statement = getDbConnection().createStatement();
            resultSet = statement.executeQuery(querry);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

}

public class MainController{

    ObservableList<PojoPersonal> listEntrant = FXCollections.observableArrayList();
    Connection connection = new DBHandler().getDbConnection();

    @FXML
    private TableColumn<PojoPersonal, String> FIO;

    @FXML
    private TableColumn<PojoPersonal, Integer> ID;

    @FXML
    private TableColumn<PojoPersonal, String> address;

    @FXML
    private TableColumn<PojoPersonal, String> birthdate;

    @FXML
    private Button personalDataBtnAdd;

    @FXML
    private TableView<PojoPersonal> personalDataTable;

    @FXML
    private TableColumn<PojoPersonal, String> telephone;

    @FXML
    private TableView<ObservableList> personalDataTable1;

    @FXML
    private TableView<ObservableList> educationTable;

    @FXML
    private TableView<ObservableList> parentsTable;


    public MainController() throws SQLException, ClassNotFoundException {
    }

    void reloadTablePersonal() throws SQLException {
        listEntrant.clear();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM entrant");
        while (resultSet.next()){
            listEntrant.add(new PojoPersonal(resultSet.getInt("id_entrant"), resultSet.getString("full_name"),
                    resultSet.getString("birthdate"), resultSet.getString("address"), resultSet.getString("telephone")));
        }
        personalDataTable.setItems(listEntrant);
    }

    @FXML
    void initialize() {
        FIO.setCellValueFactory(new PropertyValueFactory<>("FIO"));
        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        birthdate.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        try {
            reloadTablePersonal();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String requestPersonalData = "SELECT id_entrant as ID, lastname as ??????????????, firstname as ??????, patronymic as ????????????????, birthdate as '???????? ????????????????', " +
                "birthplace as '?????????? ????????????????', passport_series as '?????????? ????????????????', passport_number as '?????????? ????????????????', issued_by as '?????? ??????????', " +
                "when_issued as '???????? ????????????', birth_certificate_series as '?????????? ?????????????????????????? ?? ????????????????'," +
                " birth_certificate_number as '?????????? ?????????????????????????? ?? ????????????????' FROM personal_data";
        try {
            fill(requestPersonalData, personalDataTable1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String requestEducation = "SELECT id_entrant as ID, education_lvl as '?????????????? ??????????????????????', vertificate_number as '?????????? ??????????????????', " +
                "vertificate_mark as '?????????????? ???????? ??????????????????', profession_code as '?????? ??????????????????', profession_name as '???????????????? ??????????????????' FROM education";
        try {
            fill(requestEducation, educationTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String requestParents = "SELECT id_entrant as ID, status as '???????????? ??????????', children_amount as '???????????????????? ??????????', " +
                "fullname_mother as '?????? ????????', address_mother as '?????????? ???????????????????? ????????', number_mother as '?????????????? ????????', " +
                "job_mother as '?????????? ???????????? ????????', fullname_father as '?????? ????????', address_father as '?????????? ???????????????????? ????????', " +
                "number_father as '?????????????? ????????', job_father as '?????????? ???????????? ????????'  FROM parents";
        try {
            fill(requestParents, parentsTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void fill(String querry, TableView<ObservableList> personalDataTable1) throws SQLException {
        personalDataTable1.getColumns().clear();
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        DBHandler dbHandler = new DBHandler();
        ResultSet resultSet = dbHandler.querry(querry);
        for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(resultSet.getMetaData().getColumnLabel(i + 1));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });
            personalDataTable1.getColumns().addAll(col);
        }
        while (resultSet.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                row.add(resultSet.getString(i));
            }
            data.add(row);
        }
        personalDataTable1.setItems(data);
    }




}