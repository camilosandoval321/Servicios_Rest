package co.edu.unipiloto.ws.agendaws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Clase Persona — modelo de datos para la agenda.
 *
 * El salario se calcula automáticamente en el setter de edad
 * usando la fórmula:  salario = edad * SALARIO_MINIMO / 3
 *
 */
@XmlRootElement(name = "persona")
@XmlType(propOrder = {"id", "fullName", "age", "salary"})
public class Persona {

    // Constante: salario mínimo
    public static final double SALARIO_MINIMO = 2_000_000.0;

    // Atributos
    private int    id;
    private String fullName;
    private int    age;
    private double salary;   //  atributo nuevo, calculado automáticamente

    // Constructor vacío
    public Persona() {
    }

    // Constructor
    public Persona(int id, String fullName, int age) {
        this.id       = id;
        this.fullName = fullName;
        setAge(age);   // setAge calcula el salario automáticamente
    }

    // Getters y Setters

    @XmlElement
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Al asignar la edad se recalcula el salario automáticamente.
     * Fórmula: salario = edad * SALARIO_MINIMO / 3
     */
    @XmlElement
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age    = age;
        this.salary = age * SALARIO_MINIMO / 3.0;   //  cálculo automático
    }

    /**
     * Salario calculado. Solo lectura desde la perspectiva REST.
     */
    @XmlElement
    public double getSalary() {
        return salary;
    }

    /**
     * Setter de salary requerido por JAXB.
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }

    // toString para depuración
    @Override
    public String toString() {
        return "Persona{id=" + id
                + ", fullName='" + fullName + '\''
                + ", age=" + age
                + ", salary=" + salary + '}';
    }
}
