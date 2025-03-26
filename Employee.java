/* Class Employee {

string name;

 int age;

 float salary;

public : string getName();

void setName(string name);

 int getAge();

void setAge(int age);

float getSalary();

void setSalary(float salary);

 };*/

/*
 * Issues in the initial class:
 * 
 * 1. Public Variables:
 *    - string name;
 *    - int age;
 *    - float salary;
 *    
 *    These fields are public, meaning they can be accessed and modified 
 *    directly from outside the class, violating the principles of encapsulation.
 * 
 * 2. No Encapsulation:
 *    - The absence of private access modifiers means that there is no control 
 *      over how the data is accessed or modified.
 * 
 * so this is not object oriented.
 * below provided code is Purely Object Oriented
 */
 

 class Employee {
    private String name;
    private int age;
    private float salary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age > 0) {
            this.age = age;
        }
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        if (salary >= 0) {
            this.salary = salary;
        }
    }
}

