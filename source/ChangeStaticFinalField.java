import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by ashrafzyanov on 7/11/17.
 */
public class ChangeStaticFinalField {

    public static void main(String[] args) throws Exception {
        System.out.println(Country.name);
        changeFinalValue();
        System.out.print(Country.name);
    }

    private static void changeFinalValue() throws Exception {
        Field field = Country.class.getField("name");
        field.setAccessible(true);
        Field fieldModif = Field.class.getDeclaredField("modifiers");
        fieldModif.setAccessible(true);
        fieldModif.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, "America");
    }
}


class Country {
    public static final String name = new String("USA");

}
