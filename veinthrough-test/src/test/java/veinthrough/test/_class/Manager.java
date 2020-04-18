package veinthrough.test._class;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Objects;

/**
 *
 * @author veinthrough
 * @see ClassTest
 * @see DerivedTest
 *
 */
//子类中用lombok生成的构造函数不会包含父类中的域
//@RequiredArgsConstructor
public class Manager extends Employee
{
    @NonNull @Getter @Setter private Double bonus;

    // 5. 如果子类的构造函数没有显示调用父类的构造函数，会默认调用无参构造函数
    public Manager(String name, Double salary, Double bonus) {
        super(name,salary);
        this.bonus = bonus;
    }

    @Builder
    public Manager(String name, Double salary, String hobby, Double bonus) {
        super(name,salary,hobby);
        this.bonus = bonus;
    }

    // 9. 在覆盖一个方法时，子类方法不能低于超类方法的可见性
    public Double getSalary()
    {
        // 6. 子类不能访问父类的private域，只能调用父类的域访问器
        double baseSalary = super.getSalary();
        return baseSalary + bonus;
    }

    // 13. 子类拥有自己的相等概念，则对称性需求将强制采用getClass检测而不能用instanceof
    @Override
    public boolean equals(Object otherObject)
    {
        if (!super.equals(otherObject)) return false;
        Manager other = (Manager) otherObject;
        // super.equals checked that this and other belong to the same class
        return Objects.equals(bonus, other.bonus);
    }

    @Override
    public int hashCode()
    {
        return super.hashCode() + 17 * bonus.hashCode();
    }

    @Override
    public String toString()
    {
        return super.toString() + "[bonus=" + bonus + "]";
    }
}

