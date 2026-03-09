package domiksad.parcel_dispatch;

import domiksad.parcel_dispatch.domain.presentation.HandleUser;

import java.util.Scanner;

public class Main {
    static void main() {
        HandleUser app = new HandleUser(new Scanner(System.in));
        app.run();
    }
}
