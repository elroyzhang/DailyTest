package designmode.oberver;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by elroy on 16-10-16.
 */
public class Teacher implements Observe {
    private ArrayList<Listener> students;
    private Listener currentStudent;

    public Teacher()
    {
        this.students = new ArrayList<Listener>();
        this.currentStudent = new Student();
    }
    @Override
    public void add(Listener listen) {
        if(listen != null)
        {
            students.add(listen);
        }
    }

    @Override
    public void delete(Listener listen) {
        Iterator<Listener> it = students.iterator();
        while(it.hasNext())
        {
            if(it.next().equals(listen))
            {
                it.remove();
            }
        }
    }

    @Override
    public void notifyListener(Listener listen) {
        for(Object student: students)
        {
            if(student.equals(listen))
            {
                ((Student)student).invoke();
            }
        }
    }

    public void handle(Listener curl)
    {
        notifyListener(curl);
        this.currentStudent = curl;
    }
}
