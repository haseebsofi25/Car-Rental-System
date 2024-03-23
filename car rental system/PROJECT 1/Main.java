import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Stores Car Details
class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    //Constructors: To initialize the object in the class And to return value
    public Car(String carId, String brand, String model, double basePricePerDay) { // this is used as current obj (to insert values into specific current working objs)
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
        //We use true because it is a boolean value

    }
    // to Get the values stored
    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }
}

//Stores Customer Details
class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

//Combines both customer and car class. Stores values such as which customer takes which car
class Rental {
    //Class is a type of data type not primitive tho, object is type of class
    private Car car; //Here car is the variable of Car class
    private Customer customer;
    private int days; //no. of days taken for rent

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

//For inputing and storing Cars
class CarRentalSystem {
    private List<Car> cars; //List to store car data
    private List<Customer> customers;
    private List<Rental> rentals; //Car rented by the customer data

    //We built a constructor to store the list in memory
    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    //.add - adds a value to the array list. appends the value to the last.
    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    //Stores both customer and cars details along with rental days and checks whether the car is available for renting.
    public void rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent(); //Calls rent() function
            rentals.add(new Rental(car, customer, days)); //adds the car for renting to the list

        } else {
            System.out.println("Car is not available for rent.");
        }
    }

    //For Returning the car
    public void returnCar(Car car) {
        car.returnCar(); //calls the return car function through which we get to know that car is available for rent
        Rental rentalToRemove = null; //Here we make Rental variable of rental class and use loops to iterate it
        for (Rental rental : rentals) {
            if (rental.getCar() == car) { //getcar() will return us the car and check if the car is available or not.
                rentalToRemove = rental; // here we know that the customer has returned the car or not
                break;
            }
        }
        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);

        } else {
            System.out.println("Car was not rented.");
        }
    }

    //In the end we make an interface for the user to Select the car he wants to rent.
    public void menu() {
        Scanner scanner = new Scanner(System.in); //here we take input from the user

        while (true) { //the menu will work until true i.e until not exited
            System.out.println("===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.println("\n== Rent a Car ==\n");
                System.out.print("Enter your name: ");
                String customerName = scanner.nextLine();
                //We create a variable customerName and store details in that

                System.out.println("\nAvailable Cars:");
                for (Car car : cars) {
                    if (car.isAvailable()) {
                        System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
                    }
                }

                System.out.print("\nEnter the car ID you want to rent: ");
                String carId = scanner.nextLine();

                System.out.print("Enter the number of days for rental: ");
                int rentalDays = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
                //(customers.size() + 1) shows id in numerical order
                addCustomer(newCustomer);

                //Now we check if the car is available or not
                Car selectedCar = null;
                for (Car car : cars) {
                    if (car.getCarId().equals(carId) && car.isAvailable()) {
                        selectedCar = car;
                        break;
                    }
                }

                if (selectedCar != null) {
                    double totalPrice = selectedCar.calculatePrice(rentalDays);
                    //Here we check the total price for renting
                    System.out.println("\n== Rental Information ==\n");
                    System.out.println("Customer ID: " + newCustomer.getCustomerId());
                    System.out.println("Customer Name: " + newCustomer.getName());
                    System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
                    System.out.println("Rental Days: " + rentalDays);
                    System.out.printf("Total Price: $%.2f%n", totalPrice);

                    System.out.print("\nConfirm rental (Y/N): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("Y")) {
                        //equalsIgnoreCase - ignores case senstivity
                        rentCar(selectedCar, newCustomer, rentalDays);
                        System.out.println("\nCar rented successfully.");
                    } else {
                        System.out.println("\nRental canceled.");
                    }
                } else {
                    System.out.println("\nInvalid car selection or car not available for rent.");
                }
            } else if (choice == 2) {
                System.out.println("\n== Return a Car ==\n");
                System.out.print("Enter the car ID you want to return: ");
                String carId = scanner.nextLine();

                Car carToReturn = null;
                for (Car car : cars) {
                    if (car.getCarId().equals(carId) && !car.isAvailable()) {
                        carToReturn = car;
                        break;
                    }
                }

                if (carToReturn != null) {
                    Customer customer = null;
                    for (Rental rental : rentals) {
                        if (rental.getCar() == carToReturn) {
                            customer = rental.getCustomer();
                            break;
                        }
                    }

                    if (customer != null) {
                        returnCar(carToReturn);
                        System.out.println("Car returned successfully by " + customer.getName());
                    } else {
                        System.out.println("Car was not rented or rental information is missing.");
                    }
                } else {
                    System.out.println("Invalid car ID or car is not rented.");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        System.out.println("\nThank you for using the Car Rental System!");
    }

}
public class Main{
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();
        //First we make an object for the CarRentalSystem class


        Car car1 = new Car("C001", "Toyota", "Land Cruiser", 90.0); // Different base price per day for each car
        Car car2 = new Car("C002", "Nissan", "Patrol", 100.0);
        Car car3 = new Car("C003", "Porche", "911", 150.0);
        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);

        rentalSystem.menu();
    }
}