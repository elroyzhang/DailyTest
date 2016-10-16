package designmode.oberver;

/**
 * Created by elroy on 16-10-16.
 */
public class ObserveListenMain {
    public static void main(String[] args)
    {
        Teacher teacher = new Teacher();
        Student s1 = new Student("xiaoming");
        Student s2 = new Student("xiaozhang");
        Student s3 = new Student("xiaohua");

        teacher.add(s1);
        teacher.add(s2);
        teacher.add(s3);

        teacher.handle(new Student("default"));
        teacher.delete(s1);
        teacher.handle(new Student("xiaoming"));
        teacher.handle(new Student("xiaohua"));
    }
}
