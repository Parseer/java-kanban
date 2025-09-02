public class Main {

    public static void main(String[] args) {
        //System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();

        taskManager.addTask("Зачада 1", "Описание1", TaskStatus.NEW);
        taskManager.addTask("Зачада 2", "Описание2", TaskStatus.NEW);

        taskManager.addEpic("Epic Задача 3", "Описание3", TaskStatus.NEW);
        taskManager.addEpic("Epic Зачада 4", "Описание4", TaskStatus.NEW);

        taskManager.addSubtask("Sub Зачада 5", "Описание2", TaskStatus.NEW, taskManager.getEpic(3));
        taskManager.addSubtask("Sub Зачада 6", "Описание2", TaskStatus.NEW, taskManager.getEpic(4));
        taskManager.addSubtask("Sub Зачада 7", "Описание2", TaskStatus.NEW, taskManager.getEpic(4));

        // печать
        System.out.println("--------Печать--------");
        System.out.println(taskManager.getTasks()); //Полная печать
        System.out.println(taskManager.getListTask());
        System.out.println(taskManager.getListTEpic());
        System.out.println(taskManager.getListSubtask());
        System.out.println();
        System.out.println("--------Обновление--------");
        // обнорвление статуса тест
        System.out.println(taskManager.getEpic(4).getNameTask() + ": " + taskManager.getEpic(4).getStatus());
        System.out.println(taskManager.getSubtask(6).getNameTask() + ": " + taskManager.getSubtask(6).getStatus());
        taskManager.setStatusTask(1, TaskStatus.DONE); //Меняем статус задачи
        taskManager.setStatusSubtask(6, TaskStatus.DONE); //Меняем статус одной из задач
        System.out.println();
        System.out.println(taskManager.getTask(1).getNameTask() + ": " + taskManager.getTask(1).getStatus());
        System.out.println(taskManager.getEpic(4).getNameTask() + ": " + taskManager.getEpic(4).getStatus());
        System.out.println(taskManager.getSubtask(6).getNameTask() + ": " + taskManager.getSubtask(6).getStatus());
        System.out.println();

        //удаление
        System.out.println("--------Удаление--------");
        taskManager.deleteTask(1);
        taskManager.deleteSubtask(6);
        taskManager.deleteSubtask(7);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpic(4).getNameTask() + ": " + taskManager.getEpic(4).getStatus());
        System.out.println(taskManager.getEpic(4).getList().isEmpty()); //проверка на пусто

        System.out.println("--------Полная отчистка--------");
        taskManager.deleteAllTask();
        System.out.println(taskManager.getTasks());

    }
}
