package com.ayndev.loginandsignupone;

class Otakunz {

    private String nameUser, emailUser, passUser;

    Otakunz(String nameUser, String emailUser, String passUser) {
        setNameUser(nameUser);
        setEmailUser(emailUser);
        setPassUser(passUser);
    }

    private void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }
    String getNameUser() {
        return this.nameUser;
    }

    private void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }
    String getEmailUser() {
        return this.emailUser;
    }

    private void setPassUser(String passUser) {
        this.passUser = passUser;
    }
    String getPassUser() {
        return this.passUser;
    }
}
