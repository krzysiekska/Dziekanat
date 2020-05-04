import java.io.Serializable;

public class Dziekanat implements Serializable {
    private static final long serialVersionUID = 1L;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    private String studentName;
    private double average;
    public Dziekanat(String studentName,double average){
        this.studentName=studentName;
        this.average=average;
    }

}
