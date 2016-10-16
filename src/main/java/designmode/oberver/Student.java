package designmode.oberver;

/**
 * Created by elroy on 16-10-16.
 */
public class Student implements Listener{
    private String name = null;

    public Student()
    {
        name = new String("default");
    }
    public Student(String str)
    {
        this.name = str;
    }

    private void setName(String newname)
    {
        this.name = newname;
    }
    private String getName()
    {
        return this.name;
    }
    @Override
    public void invoke() {
        System.out.println("i am "+this.name);
    }
    @Override
    public boolean equals(Object obj)
    {
       return  ((Student)obj).getName().equals(this.name);
    }
}
