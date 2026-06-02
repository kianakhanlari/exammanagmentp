package ir.maktab.ui;

import ir.maktab.model.*;
import ir.maktab.model.dtos.*;
import ir.maktab.service.*;
import ir.maktab.service.Timer;

import ir.maktab.util.ExamFileWriter;
import ir.maktab.util.Initializer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
@Component
public class Menue {

    Scanner sc = new Scanner(System.in);

    private final UserService userService;
    private   Initializer initializer;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final CourseService courseService;
    public final ExamService examService;


    public Menue(UserService userService,
                 TeacherService teacherService,
                 StudentService studentService,
                 CourseService courseService,
                 ExamService examService,
                 Initializer initializer) {

        this.userService = userService;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.examService = examService;
        this.initializer = initializer;
    }

    public void mainMenu() {

        initializer.initialize();
        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit");

            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1 -> registerMenu();
                case 2 -> loginMenu();
                case 0 -> {
                    System.out.println("Exiting the program");
                    return;
                }
                default -> System.out.println("Invalid option");
            }
        }


    }

    public void registerMenu() {

        while (true) {
            System.out.println("\n--- Registration ---");
            System.out.println("1. Register Student");
            System.out.println("2. Register Teacher");
            System.out.println("0. Back");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    StudentDto studentDto = new StudentDto();
                    System.out.println("Enter student's full name:");
                    String name = sc.nextLine();
                    studentDto.fullName = name;

                    System.out.println("Enter username:");
                    String username = sc.nextLine();
                    studentDto.userName = username;

                    System.out.println("Enter password:");
                    String password = sc.nextLine();
                    studentDto.passWord = password;


                    System.out.println("Enter student number:");
                    String studentNumber = sc.nextLine();
                    studentDto.studentNumber = studentNumber;
                    studentService.registerStudent(studentDto);
                }
                case 2 -> {
                    TeacherDto teacherDto = new TeacherDto();
                    System.out.println("Enter teacher's full name:");
                    String name = sc.nextLine();
                    teacherDto.fullName = name;

                    System.out.println("Enter username:");
                    String username = sc.nextLine();
                    teacherDto.userName = username;

                    System.out.println("Enter password:");
                    String password = sc.nextLine();
                    teacherDto.passWord = password;
                    System.out.println("Enter the subject they teach:");
                    String speci = sc.nextLine();
                    teacherDto.specialty = speci;


                    teacherService.registerTeacher(teacherDto);
                }

                case 0 -> {
                    return;
                }

                default -> System.out.println("Invalid option");
            }

        }
    }

    public void loginMenu() {

        while (true) {

            System.out.println("Enter username:");
            String username = sc.nextLine();

            System.out.println("Enter password:");
            String password = sc.nextLine();

            UserDto userDto = new UserDto(username, password);


            try {
                User user = userService.login(userDto);

                if (user.getRole() == Role.Student) {
                    System.out.println("Student login: " + user.getFullName());
                    studentMenue(user);
                } else if (user.getRole() == Role.Professor) {
                    System.out.println("Teacher login: " + user.getFullName());
                    teacherMenue(user);
                } else if (user.getRole() == Role.Admin) {
                    System.out.println("Admin login");
                    adminMenu();
                }
                break;
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }


    }


    public void studentMenue(User user) {
        while (true) {
            System.out.println("\n----- student menue -----");
            System.out.println("1) View Available Exams");
            System.out.println("2)exite");

            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1 -> {
                    try {
                        List<Exam> exams = courseService.findExamsByStudentId(user.getUserID());
                        if (exams.isEmpty()) {
                            System.out.println("You should enroll in at least one course");
                        } else {
                            for (int i = 0; i < exams.size(); i++) {
                                System.out.println((i + 1) + ". " + exams.get(i).getTitle());
                            }

                            System.out.println("Select exam number: ");
                            int choic = sc.nextInt();
                            if (choic < 1 || choic > exams.size()) {
                                System.out.println("Invalid selection");
                            } else {
                                Exam selectedExam = exams.get(choic - 1);
                                openExam(selectedExam, user);

                            }
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Please enter a valid number");
                        sc.nextLine();
                    } catch (RuntimeException e) {
                        System.out.println("An unexpected error occurred: " + e.getMessage());
                    }


                }
                case 2 -> {
                    return;
                }

            }

        }

    }

    public void openExam(Exam selectedExam, User user) {


        ExamAttempt attempt =
                examService.findAttemptByExamIdByStudentId(selectedExam.getId(), user.getUserID());

        if (attempt == null) {

            System.out.println("1- Start Exam");
            int c = sc.nextInt();
            sc.nextLine();

            if (c == 1)
                questionMenue(selectedExam, user);

            return;
        }
        switch (attempt.getStatus()) {

            case IN_PROGRESS:
                System.out.println("1- Continue Exam");
                System.out.println("0- Back");

                int choice = sc.nextInt();

                if (choice == 1)
                    continueExam(selectedExam, user, attempt);
                break;

            case FINISHED:
                System.out.println("You already finished this exam.");
                break;

            case TIME_OUT:
                System.out.println("Time is up.");
                break;
        }
    }

    public void continueExam(Exam exam, User user, ExamAttempt attempt) {
        // ابتدا تایمر
        Timer timer = new Timer(attempt.getRemainingTime());
        Thread t = new Thread(timer);
        t.setDaemon(true);
        t.start();

        List<Question> questions = examService.findQuestionsByExamId(exam.getId());
        int index = attempt.getCurrentQuestionIndex();
        boolean examFinished = false;

        // پیدا کردن ExamStudent مربوطه
        ExamStudent examStudent = examService.findExamStudentByStudentIdAndExamId(user.getUserID(), exam.getId());
        Student student = studentService.findStudentByUserId(user.getUserID());
        examStudent.setStudent(student);
        examStudent.setExam(exam);

        attempt.setExamStudent(examStudent);

        while (index >= 0 && index < questions.size() && !examFinished) {
            System.out.println("\nTime Left: " + timer.getRemainingTime());
            System.out.println((index + 1) + ") " + questions.get(index).getText());
            System.out.println("1- Answer");
            System.out.println("2- Next");
            System.out.println("3- Previous");
            System.out.println("4- Finish Exam");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Your answer: ");
                    String ans = sc.nextLine();

                    AnswerDto answerDto = new AnswerDto();
                    answerDto.response = ans;
                    answerDto.studentId = user.getUserID();
                    answerDto.exam = exam;
                    answerDto.question = questions.get(index);
                    examService.saveAnswer(answerDto);

                    attempt.setStatus(Status.IN_PROGRESS);
                    attempt.setCurrentQuestionIndex(index);
                    attempt.setRemainingTime(timer.getRemainingTime());

                    if (attempt.getId() == null) {
                        examService.saveExamAttempt(attempt);
                    } else {
                        examService.updateAttempt(attempt); // update
                    }

                    if (index < questions.size() - 1) index++;
                    break;

                case 2:
                    if (index < questions.size() - 1) index++;
                    else System.out.println("This is the last question.");
                    break;

                case 3:
                    if (index > 0) index--;
                    else System.out.println("This is the first question.");
                    break;

                case 4:
                    System.out.println("Exam finished.");
                    examFinished = true;
                    timer.stop();
                    attempt.setStatus(Status.FINISHED);
                    attempt.setRemainingTime(timer.getRemainingTime());
                    examService.updateAttempt(attempt);
                    break;

                default:
                    System.out.println("Invalid choice");
            }
        }

        ExamStudent updatedExamStudent = examService.correctingPaper(user.getUserID(), exam.getId());
        examService.updateExamStudent(updatedExamStudent);
    }


    public void questionMenue(Exam selectedExam, User user) {
        boolean examFinished = false;


        Timer timer = new Timer();
        Thread t = new Thread(timer);
        t.setDaemon(true);
        t.start();

        List<Question> questions = examService.findQuestionsByExamId(selectedExam.getId());


        int index = 0;


        while (index >= 0 && index < questions.size() && !examFinished) {

            System.out.println("\nTime Left: " + timer.getRemainingTime());
            System.out.println((index + 1) + ")" + questions.get(index).getText());
            System.out.println("1- Answer");
            System.out.println("2- Next");
            System.out.println("3- Previous");
            System.out.println("4- Finish Exam");

            int choice = sc.nextInt();
            sc.nextLine();
            ExamAttempt examAttempt = examService.findAttemptByExamIdByStudentId(selectedExam.getId(), user.getUserID());
            switch (choice) {

                case 1:

                    if (examAttempt == null) {

                        examAttempt = new ExamAttempt();

                        ExamStudent examStudent = examService.findExamStudentByStudentIdAndExamId(user.getUserID(), selectedExam.getId());




                        System.out.print("Your answer: ");
                        String ans = sc.nextLine();
                        AnswerDto answerDto = new AnswerDto();
                        answerDto.response = ans;
                        answerDto.studentId = user.getUserID();
                        answerDto.exam = selectedExam;
                        answerDto.question = questions.get(index);

                        examService.saveAnswer(answerDto);

                          examAttempt.setExamStudent(examStudent);

                        examAttempt.setCurrentQuestionIndex(index);

                        examAttempt.setRemainingTime(timer.getRemainingTime());
                        examService.saveExamAttempt(examAttempt);
                        index++;
                    } else {
                        System.out.print("Your answer: ");
                        String ans = sc.nextLine();
                        AnswerDto answerDto = new AnswerDto();

                        answerDto.response = ans;

                        answerDto.studentId = user.getUserID();
                        answerDto.exam = selectedExam;
                        answerDto.question = questions.get(index);

                        examService.saveAnswer(answerDto);

                        examAttempt.setCurrentQuestionIndex(index);


                        if (index == questions.size() - 1) {
                            examAttempt.setStatus(Status.FINISHED);


                            examAttempt.setRemainingTime(timer.getRemainingTime());

                            examService.updateAttempt(examAttempt);
                            index++;


                        }

                    }
                    break;


                case 2:
                    if (index < questions.size() - 1) {
                        index++;

                        System.out.println("This is the last question.");
                    }
                    break;

                case 3:
                    if (index > 0) {
                        index--;

                        System.out.println("This is the first question.");
                    }
                    break;

                case 4:
                    System.out.println("Exam finished.");
                    examFinished = true;
                    timer.stop();
                    //examAttempt.setStatus(Status.FINISHED);
                    // examAttempt.setRemainingTime(timer.getRemainingTime());
                    //examService.updateAttempt(examAttempt);

                    break;

                default:
                    System.out.println("Invalid choice");
            }
        }


        ExamStudent examStudent = examService.correctingPaper(user.getUserID(), selectedExam.getId());
        examService.updateExamStudent(examStudent);
    }


    public void teacherMenue(User user) {
        while (true) {
            System.out.println("\n--- منوی استاد ---");
            System.out.println("1. مشاهده دوره‌های استاد");
            System.out.println("2.لیست ازمون های  مربوط به یک دوره  ");
            System.out.println("3.اضافه کردن ازمون جدید");
            System.out.println("4.حذف یک ازمون");
            System.out.println("5.ویرایش یک ازمون");
            System.out.println("6.پیداکردن سوال  از طریق عنوانش");
            System.out.println("7.ویزایش سوال");
            System.out.println("8.عنوان  ازمونی که میخواهید سوالاتش رو دانلود کنید وارد کنید: ");
            System.out.println("9.  تعدادو نام افراد شرکت کننده در یک آزمون و نتیجه آزمون  ");
            System.out.println("10.تصیح ازمون ");
            System.out.println("0.بازگشت به منوی قبل");
            System.out.print("انتخاب کنید: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    List<Course> cours = courseService.findCourseByTeacherId(user.getUserID());
                    if (cours.isEmpty()) {
                        System.out.println("❌ هیچ دوره‌ای برای این استاد ثبت نشده است.");
                    } else {
                        System.out.println("📚 دوره‌های شما:");
                        for (Course c : cours) {
                            System.out.println("- " + c.getTitle());
                        }

                    }
                }
                case 2 -> {
                    System.out.println("عنوان دوره وارد کنید");
                    String title = sc.nextLine();
                    List<Exam> exam = examService.findAllExamesByCourseTitle(title);
                    if (exam == null) {
                        System.out.println("❌ هیچ آزمونی انتخاب نشده است.");
                        System.out.println("ابتدا یک آزمون ایجاد یا انتخاب کنید.");

                    }

                    for (Exam e : exam) {
                        System.out.println("عنوان ازمون" + e.getTitle() + "تاریخ ازمون" + e.getExamDate());
                    }
                }
                case 3 -> {

                    Exam exam = new Exam();
                    System.out.println("عنوان ازمون وارد کنید:");
                    String title = sc.nextLine();
                    System.out.println("این ازمون مال چه دوره ی هست عنوانش وارد کنید");
                    String title1 = sc.nextLine();
                    Course c = courseService.findCourseByTitle(title1);
                    if (c == null) {
                        System.out.println("هیچ دوره ای با این عنوان وجود ندارد");
                    }
                    System.out.println("تعداد سوالات این ازمون چندتاست:");
                    int ca = sc.nextInt();
                    sc.nextLine();
                    exam.setTotalQuestions(ca);
                    exam.setCourse(c);
                    exam.setTitle(title);
                    System.out.println("زمان ازمون وارد کنید:");
                    String output = sc.nextLine();
                    LocalDate examTime = LocalDate.parse(output);
                    exam.setExamDate(examTime);
                    System.out.println("دانش اموزان مجاز برای شرکت در این ازمون ");

                    String usernames = sc.nextLine();
                    String[] studentNames = usernames.split(",");
                    List<Student> students = new ArrayList<>();
                    for (String name : studentNames) {

                        Student student = studentService.findStudentByUserName(name.trim());
                        if (student != null) {
                          /*  ExamStudent examStudent = new ExamStudent();
                            examStudent.setExam(exam);
                            examStudent.setStudent(student);
                            exam.getExamStudents().add(examStudent);
                            student.getExamStudents().add(examStudent);
                            examService.saveExamStudent(examStudent);
                           */

                            students.add(student);
                        } else {
                            System.out.println("Student not found: " + name);
                        }

                    }

                    examMenu(exam, ca, students);
                }
                case 4 -> {
                    System.out.println("عنوان ازمونی که میخواهید حذذف کنید واردکنید:");
                    String titelExam = sc.nextLine();
                    examService.removeExamByTitle(titelExam);

                }
                case 5 -> {

                    Exam exam = new Exam();
                    System.out.println("عنوان ازمونی که میخواهید ویرایش کنید وارد کنید:");
                    String titelExam = sc.nextLine();
                    exam.setTitle(titelExam);
                    System.out.println("تاریخ ازمون اصلاح کن:");
                    String output = sc.nextLine();
                    LocalDate examTime = LocalDate.parse(output);
                    exam.setExamDate(examTime);
                    examService.updateExamByTitle(exam);

                }
                case 6 -> {
                    System.out.println("عنوان سوال وارد کنید:");
                    String titelQuestion = sc.nextLine();
                    Question question = examService.findQuestionByTitle(titelQuestion);

                    if (question == null) {
                        System.out.println("❌ سوالی با این عنوان یافت نشد.");
                    } else {
                        System.out.println(question.getText());
                    }

                }
                case 7 -> {

                    System.out.println("عنوان سوال برای ویرایش وارد :");
                    String titelQuestion = sc.nextLine();
                    Question question = examService.findQuestionByTitle(titelQuestion);
                    if (question == null) {
                        System.out.println("❌ سوالی با این عنوان یافت نشد.");
                    } else {
                        System.out.println("صورت سوال تغییر بده");
                        String text = sc.nextLine();
                        question.setText(text);
                        examService.updateQuestion(question);
                    }

                }
                case 8 -> {
                    System.out.println("عنوان ازمون واد کنید:");
                    ExamFileWriter examFileWriter = new ExamFileWriter();
                    String title = sc.nextLine();
                    List<Question> questions = examService.findQuestionsByExamTitle(title);
                    if (questions.isEmpty()) {
                        System.out.println("هیچ ازمونی با این عنوان جود ندارد");
                    }
                    for (Question q : questions) {
                        examFileWriter.files(q.getText());

                    }

                }
                case 9 -> {
                    System.out.println("عنوان ازمون وارد کنید:");
                    String title = sc.nextLine();
                    Exam exam = examService.findExamByTitleExam(title);
                    List<ExamStudent> examStudent = examService.findExamStudentsByExamId(exam.getId());
                    for (ExamStudent e : examStudent) {
                        System.out.println("نام دانش اموز:"+e.getStudent().getFullName()+"     نمره دانش اموز  :"+
                                + e.getTotalScore());
                    }


                }
                case 10 -> {
                    System.out.println("عنوان ازمونی که می خواهید تصیح کنید وارد کنید:");
                    String title = sc.nextLine();
                    Exam exam = examService.findExamByTitleExam(title);
                    System.out.println("usernameدانش اموزی که می خواهید برگش تصیح کنید وارد کنید");
                    String username = sc.nextLine();
                    Student student = studentService.findStudentByUserName(username);
                    List<Answer> answers = examService.findAnswersByStudentIdAndExamId(student.getUserID(), exam.getId());
                    for (Answer a : answers) {
                        System.out.println(a.getQuestion().getText());
                        System.out.println(a.getResponse());
                        System.out.println("نمره این سوال وارد کنید:");
                        double sco = sc.nextDouble();
                        a.setScore(sco);
                        examService.updateAnswer(a);

                    }


                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("گزینه نامعتبر");
            }

        }
    }

    public void examMenu(Exam exam, int ca, List<Student> students) {

        for (int a = 0; a < ca; a++) {

            System.out.println("1. بانک سوال");
            System.out.println("2. سوال جدید");

            String choice = sc.nextLine();

            if (choice.equals("2")) {

                Question question = new Question();

                System.out.print("عنوان: ");
                question.setTitle(sc.nextLine());

                System.out.print("نوع سوال: 1.چهارگزینه2.تشریحی");
                int input = sc.nextInt();
                sc.nextLine();

                if (input == 1) {
                    question.setTypeQuestion(Option.MULTIPLE_CHOICE);
                    System.out.print("متن سوال: ");
                    question.setText(sc.nextLine());
                    System.out.println("گزینه صحیح وارد کن:");
                    question.setCorrectAnswer(sc.nextLine());

                } else {
                    question.setTypeQuestion(Option.DESCRIPTIVE);
                    System.out.print("متن سوال: ");
                    question.setText(sc.nextLine());
                }

                System.out.print("نمره: ");
                double score = sc.nextDouble();
                sc.nextLine();

                exam.addQuestion(question, score);
            }
        }

        examService.saveExam(exam);
        for (Student s : students) {
            ExamStudent examStudent = new ExamStudent();
            Exam exam1 = examService.findExamByTitleExam(exam.getTitle());
            examStudent.setExam(exam1);
            examStudent.setStudent(s);
            examService.saveExamStudent(examStudent);

            exam.getExamStudents().add(examStudent);
            s.getExamStudents().add(examStudent);

        }


    }


    public void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. User Management");
            System.out.println("2. Search and Filter Users");
            System.out.println("3. Course Management");
            System.out.println("0. Back");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> userManagementMenu();
                case 2 -> searchMenu();
                case 3 -> {
                    courseMemberMenu();
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    public void searchMenu() {
        while (true) {
            System.out.println("\n--- Search and Filter Users ---");
            System.out.println("1. Search by Role");
            System.out.println("2. Search by Last Name");
            System.out.println("3. Combined Search (Role + Last Name)");
            System.out.println("0. Back");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("Enter the role of the users you want to view:");

                    String roleInput = sc.nextLine().trim();
                    Role roleEnum = Role.valueOf(roleInput);

                    List<User> listUser = userService.findUsersByRole(roleEnum);
                    listUser.forEach(u -> System.out.println(u.getRole() + " - " + u.getFullName()));
                }
                case 2 -> {
                    System.out.println("Enter the first and last name of the person to determine their role:");

                    String fullName = sc.nextLine();
                    Optional<User> userOpt = userService.findUserByFullName(fullName);

                    if (userOpt.isPresent()) {
                        User user = userOpt.get();
                        System.out.println("Role: " + user.getRole());
                    } else {
                        System.out.println("User not found!");
                    }
                }


                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    public void courseMemberMenu() {
        while (true) {
            System.out.println("\n========= Course Management Menu =========");
            System.out.println("1. Show all courses");
            System.out.println("2. Create a new course");
            System.out.println("3. Edit a course");
            System.out.println("4. Delete a course");
            System.out.println("7. View course participants (Instructor / Student)");
            System.out.println("0. Return to previous menu");
            System.out.print("Your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    List<Course> courses = courseService.showAllCourse();
                    for (Course cours : courses) {
                        System.out.println("=================================");
                        System.out.println("Course title: " + cours.getTitle());
                        System.out.println("start date: " + cours.getStartDate());
                        System.out.println("end date: " + cours.getEndDate());
                        System.out.println("students list:");

                        for (Student student : cours.getStudents()) {
                            System.out.println("• " + student.getFullName());
                        }
                    }
                }


                case 2 -> {
                    CourseDto courseDto = new CourseDto();

                    System.out.println("Enter course title:");
                    String title = sc.nextLine();
                    courseDto.title = title;

                    System.out.println("Enter course start date (yyyy-MM-dd):");
                    String input = sc.nextLine();
                    LocalDate startDate = LocalDate.parse(input);
                    courseDto.startDate = startDate;

                    System.out.println("Enter course end date (yyyy-MM-dd):");
                    String output = sc.nextLine();
                    LocalDate endDate = LocalDate.parse(output);
                    courseDto.endDate = endDate;

                    System.out.println("Enter teacher username:");
                    String teacherCourse = sc.nextLine();
                    Teacher teacher = teacherService.findTeacherByUserName((teacherCourse));
                    courseDto.teacher = teacher;

                    System.out.println("Enter the usernames of students you want to add to the course:");
                    System.out.println("Separate the usernames with commas (e.g., ali,reza,sara):");

                    String inputs = sc.nextLine();

                    String[] studentNames = inputs.split(",");

                    for (String name : studentNames) {
                        courseDto.students.add(name.trim());
                    }

                    courseService.registerCourse(courseDto);
                }


                case 3 -> {
                    CourseDto courseDto = new CourseDto();

                    System.out.println("Enter the title of the course you want to edit:");
                    String title = sc.nextLine();
                    courseDto.title = title;

                    System.out.println("Enter the new start date for the course (yyyy-MM-dd):");
                    String input = sc.nextLine();
                    LocalDate startDate = LocalDate.parse(input);
                    courseDto.startDate = startDate;

                    System.out.println("Enter the new end date for the course (yyyy-MM-dd):");
                    String output = sc.nextLine();
                    LocalDate endDate = LocalDate.parse(output);
                    courseDto.endDate = endDate;

                    courseService.updateCourse(courseDto);

                    System.out.println("✅ Course updated successfully!");
                }

                case 4 -> {
                    System.out.println("Enter the title of the course you want to delete:");
                    String title = sc.nextLine();
                    courseService.removeCourse(title);
                    System.out.println("✅ Course deleted successfully!");

                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invaild option");
            }
        }
    }

    public void userManagementMenu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- User Management ---");
            System.out.println("1. View Pending Users");
            System.out.println("2. Approve User");
            System.out.println("0. Back");


            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    List<User> pending = userService.getPendingUsers();

                    pending.forEach(u ->
                            System.out.println(u.getUserID() + " - " + u.getFullName() + " (" + u.getUserName() + ")")
                    );

                }
                case 2 -> {
                    System.out.println("Enter the name of the user to approve:");
                    String usrname = sc.nextLine();
                    userService.approveUser(usrname);
                    System.out.println("User has been approved.");
                }

                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

}
