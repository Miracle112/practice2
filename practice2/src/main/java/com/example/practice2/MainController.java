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

        String requestPersonalData = "SELECT id_entrant as ID, lastname as Фамилия, firstname as Имя, patronymic as Отчество, birthdate as 'День рождения', " +
                "birthplace as 'Место рождения', passport_series as 'Серия паспорта', passport_number as 'Номер паспорта', issued_by as 'Кем выдан', " +
                "when_issued as 'Дата выдачи', birth_certificate_series as 'Серия сведетельства о рождении'," +
                " birth_certificate_number as 'Номер сведетельства о рождении' FROM personal_data";
        try {
            fill(requestPersonalData, personalDataTable1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String requestEducation = "SELECT id_entrant as ID, education_lvl as 'Уровень образования', vertificate_number as 'Номер аттестата', " +
                "vertificate_mark as 'Средний балл аттестата', profession_code as 'Код профессии', profession_name as 'Название профессии' FROM education";
        try {
            fill(requestEducation, educationTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String requestParents = "SELECT id_entrant as ID, status as 'Статус семьи', children_amount as 'Количество детей', " +
                "fullname_mother as 'ФИО мамы', address_mother as 'Место жительства мамы', number_mother as 'Телефон мамы', " +
                "job_mother as 'Место работы мамы', fullname_father as 'ФИО папы', address_father as 'Место жительства папы', " +
                "number_father as 'Телефон папы', job_father as 'Место работы папы'  FROM parents";
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