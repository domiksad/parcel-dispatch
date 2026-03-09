package domiksad.parcel_dispatch.domain.model.human;

public class Person implements Human{
    private String fullName;
    private String adress;

    public Person(String fullName, String adress) {
        this.fullName = fullName;
        this.adress = adress;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    public String getAdress() {
        return adress;
    }

    @Override
    public String toString(){
        return fullName + ":" + adress;
    }
}
