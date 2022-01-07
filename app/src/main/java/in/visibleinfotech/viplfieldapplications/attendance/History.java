package in.visibleinfotech.viplfieldapplications.attendance;

class History {
    private String time1, time2;
    private String image1, image2;

    History(String time1, String image1, String time2, String image2) {
        this.time1 = time1;
        this.image1 = image1;
        this.time2 = time2;
        this.image2 = image2;
    }

    String getTime1() {
        return time1;
    }

    String getImage1() {
        return image1;
    }

    String getTime2() {
        return time2;
    }

    String getImage2() {
        return image2;
    }
}
