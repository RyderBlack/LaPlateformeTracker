package app.tracky.service;

import app.tracky.service.impl.AdminServiceImpl;
import app.tracky.service.impl.StudentServiceImpl;
import app.tracky.service.impl.StudyDomainServiceImpl;

public class ServiceFactory {
    private static AdminService adminService;
    private static StudentService studentService;
    private static StudyDomainService studyDomainService;

    public static synchronized AdminService getAdminService() {
        if (adminService == null) {
            adminService = new AdminServiceImpl();
        }
        return adminService;
    }

    public static synchronized StudentService getStudentService() {
        if (studentService == null) {
            studentService = new StudentServiceImpl();
        }
        return studentService;
    }

    public static synchronized StudyDomainService getStudyDomainService() {
        if (studyDomainService == null) {
            studyDomainService = new StudyDomainServiceImpl();
        }
        return studyDomainService;
    }
}
