public class Main {

    public static void main(String[] args) {
        /*TaskManager taskManager = Managers.getDefault();

        taskManager.addTask("Зачада 1", "Описание1", TaskStatus.NEW);
        taskManager.addTask("Зачада 2", "Описание2", TaskStatus.NEW);

        taskManager.addEpic("Epic Задача 3", "Описание3", TaskStatus.NEW);
        taskManager.addEpic("Epic Зачада 4", "Описание4", TaskStatus.NEW);

        taskManager.addSubtask("Sub Зачада 5", "Описание2", TaskStatus.NEW, taskManager.getEpic(3));
        taskManager.addSubtask("Sub Зачада 6", "Описание2", TaskStatus.NEW, taskManager.getEpic(4));
        taskManager.addSubtask("Sub Зачада 7", "Описание2", TaskStatus.NEW, taskManager.getEpic(4));
        System.out.println("История: " + taskManager.getHistory());

        // печать
        System.out.println("--------Печать--------");
        System.out.println(taskManager.getAllTasks()); //Полная печать
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println();
        System.out.println("--------Обновление--------");
        System.out.println("История: " + taskManager.getHistory());
        // обнорвление статуса тест
        System.out.println(taskManager.getEpic(4).getName() + ": " + taskManager.getEpic(4).getStatus());
        System.out.println("История: " + taskManager.getHistory());
        System.out.println(taskManager.getSubtask(6).getName() + ": " + taskManager.getSubtask(6).getStatus());
        System.out.println("История: " + taskManager.getHistory());
        taskManager.setStatusTask(1, TaskStatus.DONE); //Меняем статус задачи
        taskManager.setStatusSubtask(6, TaskStatus.DONE); //Меняем статус одной из задач
        System.out.println();
        System.out.println(taskManager.getTask(1).getName() + ": " + taskManager.getTask(1).getStatus());
        System.out.println("История: " + taskManager.getHistory());
        System.out.println(taskManager.getEpic(4).getName() + ": " + taskManager.getEpic(4).getStatus());
        System.out.println("История: " + taskManager.getHistory());
        System.out.println(taskManager.getSubtask(6).getName() + ": " + taskManager.getSubtask(6).getStatus());
        System.out.println("История: " + taskManager.getHistory());
        System.out.println();
        //удаление
        System.out.println("--------Удаление--------");
        taskManager.deleteTask(1);
        taskManager.deleteSubtask(6);
        taskManager.deleteSubtask(7);
        System.out.println("История: " + taskManager.getHistory());
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getEpic(4).getName() + ": " + taskManager.getEpic(4).getStatus());
        System.out.println(taskManager.getEpic(4).getSubtasks().isEmpty()); //проверка на пусто

        //Полная отчистка
        System.out.println("--------Полная отчистка--------");
        taskManager.deleteAllTask();
        System.out.println(taskManager.getAllTasks());

        System.out.println("История: " + taskManager.getHistory());
        System.out.println();
        System.out.println("---------Доп.Задание---------");
        //Задание 1
        TaskManager taskManagerTwo = Managers.getDefault();
        taskManagerTwo.addEpic("Epic Задача 1", "Описание1", TaskStatus.NEW);
        taskManagerTwo.addEpic("Epic Зачада 2", "Описание2", TaskStatus.NEW);
        taskManagerTwo.addSubtask("Sub Зачада 3", "Описание3", TaskStatus.NEW, taskManagerTwo.getEpic(1));
        taskManagerTwo.addSubtask("Sub Зачада 4", "Описание4", TaskStatus.NEW, taskManagerTwo.getEpic(1));
        taskManagerTwo.addSubtask("Sub Зачада 5", "Описание5", TaskStatus.NEW, taskManagerTwo.getEpic(1));
        System.out.println();
        System.out.println("----Задание 3 и 4----");
        System.out.println("История: " + taskManagerTwo.getHistory());
        System.out.println(taskManagerTwo.getEpic(1).getName() + ": " + taskManagerTwo.getEpic(1).getStatus());
        System.out.println(taskManagerTwo.getEpic(2).getName() + ": " + taskManagerTwo.getEpic(2).getStatus());
        System.out.println(taskManagerTwo.getSubtask(3).getName() + ": " + taskManagerTwo.getSubtask(3).getStatus());
        System.out.println(taskManagerTwo.getSubtask(4).getName() + ": " + taskManagerTwo.getSubtask(4).getStatus());
        System.out.println(taskManagerTwo.getSubtask(5).getName() + ": " + taskManagerTwo.getSubtask(5).getStatus());
        System.out.println("История: " + taskManagerTwo.getHistory());
        System.out.println(taskManagerTwo.getSubtask(3).getName() + ": " + taskManagerTwo.getSubtask(3).getStatus());
        System.out.println("История: " + taskManagerTwo.getHistory());
        System.out.println(taskManagerTwo.getSubtask(4).getName() + ": " + taskManagerTwo.getSubtask(4).getStatus());
        System.out.println("История: " + taskManagerTwo.getHistory());
        System.out.println(taskManagerTwo.getEpic(1).getName() + ": " + taskManagerTwo.getEpic(1).getStatus());
        System.out.println(taskManagerTwo.getEpic(1).getName() + ": " + taskManagerTwo.getEpic(1).getStatus());
        System.out.println("История: " + taskManagerTwo.getHistory());
        System.out.println();
        System.out.println("----Задание 5 и 6----");
        taskManagerTwo.deleteEpic(2);
        System.out.println("История: " + taskManagerTwo.getHistory());
        taskManagerTwo.deleteEpic(1);
        System.out.println("История: " + taskManagerTwo.getHistory());*/
    }
}
