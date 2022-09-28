import java.util.Scanner;

/**
 * This program, ClubStore, will allow the user to order some items as the order
 * is placed between October 15th and November 30th.
 * The shipping type can be selected, and depending on how much is ordered, a
 * coupon can be used as well. The program will also inform the user the order
 * was placed
 * at an invalid date or if they gave an invalid input. At the end, it will tell
 * you how much the order will cost, including shipping as well as your arrival
 * date.
 * 
 * @author Albert Seo
 */
public class ClubStore {

    /**
     * Constant that represents the number of days in a month
     */
    public static final int daysInAMonth = 31;

    /**
     * Constant that represents the how frequent a leap year occurs, which is every
     * 4 years
     */
    public static final int LEAP_YEAR_FREQUENCY = 4;

    /**
     * Constant that represents the number of days in a week
     */
    public static final int daysInAWeek = 7;

    /**
     * Constand that represents the number of months in a year
     */
    public static final int monthsInAYear = 12;

    /**
     * Constant that represents the number of days to deliver when standard shipping
     * is selected
     */
    public static final int standardShippingDays = 5;

    /**
     * Constant that represents the number of days to deliver when two-day shipping
     * is selected
     */
    public static final int twoShippingDays = 2;

    /**
     * Constant that represents the cost of one bottle
     */
    public static final int bottlePrice = 10;

    /**
     * Constant that represents the cost of one mug
     */
    public static final int mugPrice = 12;

    /**
     * Constnat that represents the cost of one bag
     */
    public static final int bagPrice = 18;

    /**
     * Checks if the given date (month and day) is valid, or between October 15th
     * and November 30th. If so, return true. Otherwise return false
     * 
     * @param month the given month (i.e. Sepetember = 9)
     * @param day   the given day
     * @return if the date is valid
     */
    public static boolean isValidDate(int month, int day) {
        switch (month) {
            case 10:
                // Checks if the given day is between 15-31 for October
                if (day >= 15 && day <= 31) {
                    return true;
                }
                break;
            case 11:
                // Checks if the given day is between 1=30 for November
                if (day >= 1 && day <= 30) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Will give an arrival date depending on when the order was placed as well as
     * the type of shipping that was chosen. Checks for invalid dates using the
     * isValidDate() method as well if the given numberOfShippingDays is too small
     * or too big. Makes use of Zeller's algorithm to determine exactly what day
     * the order will arrive, such as a Thursday
     * 
     * @param orderMonth           the given month the order was placed (i.e.
     *                             September = 9)
     * @param orderDay             the given day the order was placed
     * @param numberOfShippingDays the given number of days it will take to deliver
     *                             (i.e. 5 for standard, 2 for two-day)
     * @throws IllegalArugmentException if the given order date is not between
     *                                  October 15th and November 30th
     * @throws IllegalArugmentException if the numberOfShippingDays is less than 1
     *                                  or greater than 5
     * @return the date that the order will arrive on with the exact day
     */
    public static String getArrivalDate(int orderMonth, int orderDay, int numberOfShippingDays) {
        // Checks for any invalid inputs
        if (isValidDate(orderMonth, orderDay) == false) {
            throw new IllegalArgumentException("Invalid date");
        }
        if (numberOfShippingDays < 1 || numberOfShippingDays > 5) {
            throw new IllegalArgumentException("Invalid days");
        }

        // Checks if the arrival day goes over the amount of days in a specific month,
        // so it will arrive on 11/2 instead of 10/33
        int arrivalDay = orderDay + numberOfShippingDays;
        if (orderMonth == 10 && arrivalDay > 31) {
            orderMonth = 11;
            arrivalDay -= 31;
        }
        if (orderMonth == 11 && arrivalDay > 30) {
            orderMonth = 12;
            arrivalDay -= 30;
        }

        // Zeller's Algorithm to determine the specific day (i.e. Friday)
        int year = 2022;
        int partOne = year - ((monthsInAYear + 2) - orderMonth) / 12;
        int partTwo = partOne + partOne / LEAP_YEAR_FREQUENCY - partOne / 100 + partOne / 400;
        int partThree = orderMonth + monthsInAYear * (((monthsInAYear + 2) - orderMonth) / monthsInAYear) - 2;
        int dayNum = (arrivalDay + partTwo + (daysInAMonth * partThree) / monthsInAYear) % daysInAWeek;

        // Determines the day based on the number calculated from Zeller's Algorithm
        String day = "";
        switch (dayNum) {
            case 0:
                day = "Sun";
                break;
            case 1:
                day = "Mon";
                break;
            case 2:
                day = "Tue";
                break;
            case 3:
                day = "Wed";
                break;
            case 4:
                day = "Thu";
                break;
            case 5:
                day = "Fri";
                break;
            case 6:
                day = "Sat";
                break;
        }
        String date = day + ", " + orderMonth + "/" + arrivalDay + "/" + year;
        return date;
    }

    /**
     * Calculates the subtotal of the order based on the number of bottles, mugs, or
     * bags wanted by multiplying the number of items by their respective prices.
     * 
     * @param numberOfBottles the given number of bottles ordered
     * @param numberOfMugs    the given number of mugs ordered
     * @param numberOfBags    the given number of bags ordered
     * @throws IllegalArugmentException if numberOfBottles is less than 0
     * @throws IllegalArugmentException if numberOfMugs is less than 0
     * @throws IllegalArugmentException if numberOfBags is less than 0
     * @return the total cost of the ordered items
     */
    public static int getSubtotal(int numberOfBottles, int numberOfMugs, int numberOfBags) {
        if (numberOfBottles < 0) {
            throw new IllegalArgumentException("Invalid number");
        }
        if (numberOfMugs < 0) {
            throw new IllegalArgumentException("Invalid number");
        }
        if (numberOfBags < 0) {
            throw new IllegalArgumentException("Invalid number");
        }

        // Mulitplies the amount of items ordered by their respective prices
        int bottleTotal = numberOfBottles * bottlePrice;
        int mugTotal = numberOfMugs * mugPrice;
        int bagTotal = numberOfBags * bagPrice;
        int total = bottleTotal + mugTotal + bagTotal;
        return total;
    }

    /**
     * Determines the shipping type based on the user input. If it is standard,
     * return false. Otherwise if two-day, return true. Checks for invalid input.
     * 
     * @param type the given user input (i.e. 'S' for standard)
     * @throws IllegalArugmentException if user input does not match 's' or 't'
     * @return a boolean based on the type of shipping: false for standard, true for
     *         two-day
     */
    public static boolean shippingType(char type) {
        if (type == 's' || type == 'S') {
            return false;
        } else if (type == 't' || type == 'T') {
            return true;
        } else {
            throw new IllegalArgumentException("Invalid shipping");
        }
    }

    /**
     * Calculates the total number of items ordered by using addition
     * 
     * @param bottlesOrdered the given number of bottled ordered
     * @param mugsOrdered    the given number of mugs ordered
     * @param bagsOrdered    the given number of bags ordered
     * @return the total number of items ordered
     */
    public static int totalOrdered(int bottlesOrdered, int mugsOrdered, int bagsOrdered) {
        int ordered = bottlesOrdered + mugsOrdered + bagsOrdered;
        return ordered;
    }

    /**
     * Calulates the shipping cost based on the type of shipping chosen, the
     * subtotal, and if the user inputted a valid coupon.
     * Standard shipping is $3, but can be removed if the user spent at least $25 or
     * redeeed a valid coupon. Two-day shipping is $5 but
     * cannot be discounted regardless of subtotal or coupon.
     * 
     * @param subtotal       the given subtotal of the order
     * @param twoDayShipping boolean if it is two-day shipping or not, obtained from
     *                       the shippingType() method
     * @param hasValidCoupon boolean if the user has a valid coupon or not, obtained
     *                       from user input
     * @throws IllegalArugmentException if the subtotal is less than 0
     * @return the shipping cost of the order
     */
    public static int getShippingCost(int subtotal, boolean twoDayShipping, boolean hasValidCoupon) {
        if (subtotal < 0) {
            throw new IllegalArgumentException("Invalid subtotal");
        }
        // standard shipping = $3, removed if subtotal if $25 or more
        // standard shipping = free with any size order if valid coupon
        // two day shipping = $5 no matter what
        int shippingCost = 0;
        if (subtotal == 0) {
            return shippingCost;
        } else if (twoDayShipping) {
            shippingCost = 5;
        } else if (hasValidCoupon) {
            shippingCost = 0;
        } else if (subtotal >= 25) {
            shippingCost = 0;
        } else {
            shippingCost = 3;
        }
        return shippingCost;
    }

    // Main method that welcomes the user and prompts for inputs. Depending on the
    // user input, will show more prompts or
    // give the receipt and arrival date.
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Welcome message, providing instructions on how to use the program
        System.out.println("                    Welcome to our Club Store!");
        System.out.println("All orders must be placed between October 15 and November 30, 2022.");
        System.out.println("When prompted, please enter today's date and the number of each");
        System.out.println("item you would like to purchase. Please enter S to choose Standard");
        System.out.println("(five-day) shipping or T to choose Two-day shipping. Orders of");
        System.out.println("$25.00 or more receive free Standard shipping. Entering the correct");
        System.out.println("coupon code also entitles you to free Standard shipping! A receipt");
        System.out.println("and the arrival date of your order will be displayed.");

        // Prompt the user to input the date and checks of invalids
        System.out.println("Month Day (e.g., 11 9): ");
        int month = input.nextInt();
        int day = input.nextInt();
        if (isValidDate(month, day) == false) {
            System.out.println("Invalid date");
            System.exit(1);
        }

        // Prompt the user to input the amount of each item they would like to purchase
        // and checks for invalids
        System.out.println("Number of Water Bottles($10.00 each): ");
        int orderedBottles = input.nextInt();
        if (orderedBottles < 0) {
            System.out.println("Invalid number");
            System.exit(1);
        }

        System.out.println("Number of Coffee Mugs($12.00 each): ");
        int orderedMugs = input.nextInt();
        if (orderedMugs < 0) {
            System.out.println("Invalid number");
            System.exit(1);
        }

        System.out.println("Number of Tote Bags($18.00 each): ");
        int orderedBags = input.nextInt();
        if (orderedBags < 0) {
            System.out.println("Invalid number");
            System.exit(1);
        }

        // Temporary variables
        int tempSubtotal = getSubtotal(orderedBottles, orderedMugs, orderedBags);
        int tempShipping = 0;
        int tempTotal = 0;
        String arrival = "";

        // Checks if at least 1 item was ordered
        if (totalOrdered(orderedBottles, orderedMugs, orderedBags) >= 1) {
            System.out.println("Shipping (S-tandard, T-wo Day): ");
            char typeOfShipping = input.next().charAt(0);

            // Checks for invalids
            if (typeOfShipping != 's' && typeOfShipping != 'S' && typeOfShipping != 't' && typeOfShipping != 'T') {
                System.out.println("Invalid shipping");
                System.exit(1);
            }

            // Checks if shipping type is standard and order size is less than 25
            if (!shippingType(typeOfShipping) && getSubtotal(orderedBottles, orderedMugs, orderedBags) < 25) {
                System.out.println("Coupon (y,n): ");
                String userAction = input.next();

                // Checks if user input started with a "y" and accounts for upper and lowercase.
                if (userAction.startsWith("y") || userAction.startsWith("Y")) {
                    System.out.println("Coupon Code: ");
                    String couponCode = input.next();

                    // Checks if the given coupon code is equal to "PACK2022"
                    if (couponCode.equals("PACK2022")) {
                        arrival = getArrivalDate(month, day, standardShippingDays);
                        tempShipping = getShippingCost(tempSubtotal, shippingType(typeOfShipping), true);
                        tempTotal = tempSubtotal + tempShipping;
                    } else {
                        // Returns if the code is invalid, but does not stop the program
                        System.out.println("Invalid code");
                        arrival = getArrivalDate(month, day, standardShippingDays);
                        tempShipping = getShippingCost(tempSubtotal, shippingType(typeOfShipping), false);
                        tempTotal = tempSubtotal + tempShipping;
                    }
                } else {
                    arrival = getArrivalDate(month, day, standardShippingDays);
                    tempShipping = getShippingCost(tempSubtotal, shippingType(typeOfShipping), false);
                    tempTotal = tempSubtotal + tempShipping;
                }
            } else if (shippingType(typeOfShipping)) {
                arrival = getArrivalDate(month, day, twoShippingDays);
                tempShipping = getShippingCost(tempSubtotal, shippingType(typeOfShipping), false);
                tempTotal = tempSubtotal + tempShipping;
            } else {
                arrival = getArrivalDate(month, day, standardShippingDays);
                tempShipping = getShippingCost(tempSubtotal, shippingType(typeOfShipping), false);
                tempTotal = tempSubtotal + tempShipping;
            }
        }

        // Converts to double for formatting purposes
        double subtotal = (double) tempSubtotal;
        double shipping = (double) tempShipping;
        double total = (double) tempTotal;

        String result = "Subtotal : $ %.2f\nShipping: $ %.2f\nTotal: $ %.2f";
        String line = String.format(result, subtotal, shipping, total);
        System.out.println(line);

        // Outputs an arrival date as long as the user ordered something
        if (totalOrdered(orderedBottles, orderedMugs, orderedBags) > 0) {
            System.out.println("Arrival date: " + arrival);
        }
    }
}
