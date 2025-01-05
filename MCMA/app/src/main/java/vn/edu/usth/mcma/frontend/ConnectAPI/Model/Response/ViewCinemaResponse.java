package vn.edu.usth.mcma.frontend.ConnectAPI.Model.Response;

import java.util.List;

public class ViewCinemaResponse {
    private List<Integer> cinemaIdList;
    private List<String> cinemaNameList;
    private List<String> cityNameList;
    private List<String> cinemaAddressList;

    public ViewCinemaResponse(List<Integer> cinemaIdList, List<String> cinemaNameList) {
        this.cinemaIdList = cinemaIdList;
        this.cinemaNameList = cinemaNameList;
    }
    public ViewCinemaResponse(List<Integer> cinemaIdList, List<String> cinemaNameList, List<String> cityNameList) {
        this.cinemaIdList = cinemaIdList;
        this.cinemaNameList = cinemaNameList;
        this.cityNameList = cityNameList;
    }

    public ViewCinemaResponse(List<String> cinemaAddressList, List<Integer> cinemaIdList, List<String> cinemaNameList, List<String> cityNameList) {
        this.cinemaAddressList = cinemaAddressList;
        this.cinemaIdList = cinemaIdList;
        this.cinemaNameList = cinemaNameList;
        this.cityNameList = cityNameList;
    }

    public List<Integer> getCinemaIdList() {
        return cinemaIdList;
    }

    public void setCinemaIdList(List<Integer> cinemaIdList) {
        this.cinemaIdList = cinemaIdList;
    }

    public List<String> getCinemaNameList() {
        return cinemaNameList;
    }

    public void setCinemaNameList(List<String> cinemaNameList) {
        this.cinemaNameList = cinemaNameList;
    }

    public List<String> getCityNameList() {
        return cityNameList;
    }

    public void setCityNameList(List<String> cityNameList) {
        this.cityNameList = cityNameList;
    }

    public List<String> getCinemaAddressList() {
        return cinemaAddressList;
    }

    public void setCinemaAddressList(List<String> cinemaAddressList) {
        this.cinemaAddressList = cinemaAddressList;
    }
}

