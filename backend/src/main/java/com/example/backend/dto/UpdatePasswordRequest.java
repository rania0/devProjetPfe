package com.example.backend.dto;

public class UpdatePasswordRequest {

        private String mail;  // 📧 البريد الإلكتروني للمستخدم
        private String oldPassword;  // 🔑 كلمة المرور القديمة
        private String newPassword;  // 🔑 كلمة المرور الجديدة

        // ✅ Getters and Setters
        public String getMail() {
                return mail;
        }

        public void setMail(String mail) {
                this.mail = mail;
        }

        public String getOldPassword() {
                return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
                this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
                return newPassword;
        }

        public void setNewPassword(String newPassword) {
                this.newPassword = newPassword;
        }
}
