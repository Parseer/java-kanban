public class Main {

    public static void main(String[] args) {
        //System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();

        taskManager.addTask("Зачада 1", "Описание1", TypesOfStatuses.NEW);
        taskManager.addTask("Зачада 2", "Описание2", TypesOfStatuses.NEW);

        taskManager.addEpic("Epic Задача 3", "Описание3", TypesOfStatuses.NEW);
        taskManager.addEpic("Epic Зачада 4", "Описание4", TypesOfStatuses.NEW);

        taskManager.addSubtask("Sub Зачада 5", "Описание2", TypesOfStatuses.NEW, taskManager.getIdEpic(3));
        taskManager.addSubtask("Sub Зачада 6", "Описание2", TypesOfStatuses.NEW, taskManager.getIdEpic(4));
        taskManager.addSubtask("Sub Зачада 7", "Описание2", TypesOfStatuses.NEW, taskManager.getIdEpic(4));

        // печать
        System.out.println("--------Печать--------");
        System.out.println(taskManager.getListAllTask()); //Полная печать
        System.out.println(taskManager.getListTask());
        System.out.println(taskManager.getListTEpic());
        System.out.println(taskManager.getListSubtask());
        System.out.println();
        System.out.println("--------Обновление--------");
        // обнорвление статуса тест
        System.out.println(taskManager.getIdEpic(4).getNameTask() + ": " + taskManager.getIdEpic(4).getTypesOfStatuses());
        System.out.println(taskManager.getIdSubtask(6).getNameTask() + ": " + taskManager.getIdSubtask(6).getTypesOfStatuses());
        taskManager.setStatusTask(1, TypesOfStatuses.DONE); //Меняем статус задачи
        taskManager.setStatusSubtask(6, TypesOfStatuses.DONE); //Меняем статус одной из задач
        System.out.println();
        System.out.println(taskManager.getIdTask(1).getNameTask() + ": " + taskManager.getIdTask(1).getTypesOfStatuses());
        System.out.println(taskManager.getIdEpic(4).getNameTask() + ": " + taskManager.getIdEpic(4).getTypesOfStatuses());
        System.out.println(taskManager.getIdSubtask(6).getNameTask() + ": " + taskManager.getIdSubtask(6).getTypesOfStatuses());
        System.out.println();

        //удаление
        System.out.println("--------Удаление--------");
        taskManager.dellByIdTask(1);
        taskManager.dellByIdSubtask(6);
        taskManager.dellByIdSubtask(7);

        System.out.println(taskManager.getListAllTask());
        System.out.println(taskManager.getIdEpic(4).getNameTask() + ": " + taskManager.getIdEpic(4).getTypesOfStatuses());
        System.out.println(taskManager.getIdEpic(4).getListSubInEpic().isEmpty()); //проверка на пусто

        System.out.println("--------Полная отчистка--------");
        taskManager.dellAllTask();
        System.out.println(taskManager.getListAllTask());

    }
}
