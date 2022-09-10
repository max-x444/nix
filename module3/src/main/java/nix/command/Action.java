package nix.command;

public enum Action {
    SEARCH_GROUPS_BY_NAME("Search groups by name", new SearchGroupsByName()),
    NUMBER_OF_STUDENTS_PER_GROUP("Number of students per group", new NumberOfStudentsPerGroup()),
    AVERAGE_SCORE_OF_EACH_GROUP("Average score of each group", new AverageScoreOfEachGroup()),
    SEARCH_TEACHER_BY_NAME_OR_SURNAME("Search teacher by name or surname", new SearchTeacherByNameOrSurname()),
    GET_SUBJECT_BEST_AND_WORST_RESULTS("Get subject best and worst results", new GetSubjectBestAndWorstResults()),
    GET_STUDENT_WHOSE_AVERAGE_IS_GREATER_THAN_VALUE("Get student whose average is greater than value", new GetStudentWhoseAverageIsGreaterThanValue()),
    EXIT("Exit", null);

    private final String name;
    private final Command command;

    Action(String name, Command command) {
        this.name = name;
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public Command execute() {
        if (command != null) {
            command.execute();
        }
        return command;
    }
}