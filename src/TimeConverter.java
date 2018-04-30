/**
 * MIT License
 *
 * Copyright (c) 2018 Kyle Kowalczyk
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This class is a rewrite of my PythonTimeConverter library on github
 * https://github.com/superadm1n/PythonTimeConverter Its purpose is to
 * allow me to experiment with Java. Methods in this class can be rewritten
 * to account for bugs but this is ment to be a work in progress
 *
 * Version: 0.0.1
 */

import java.util.Arrays;

public class TimeConverter {

    public static void main(String[] args){

        /*
        Accepts arguments and interprets them running the proper methods.
        This is designed to be run as a command line application.
         */

        String convertedTime;
        String argument;
        Boolean Flag = false;

        for (String x: args){
            // If -m2s is the argument it captures the next argument and passes
            // that to the miliToStandard method to convert the time
            if (x.equals("-m2s")){
                argument = args[Arrays.asList(args).indexOf(x) + 1];
                convertedTime = miliToStandard(argument);
                System.out.println(convertedTime);
                Flag = true;
            }
            // if -s2m is the argument it captures the next argument and passes
            // that to the standardToMili method to convert the time.
            if (x.equals("-s2m")){
                argument = args[Arrays.asList(args).indexOf(x) + 1];
                convertedTime = standardToMili(argument);
                System.out.println(convertedTime);
                Flag = true;
            }
        }

        // If there were no valid arguments found (specified by Flag equaling false
        // It prints out to the user that there was no valid argument found.
        if (Flag.equals(false)){
            System.out.println("No valid argument found!");
        }

    }

    // This method has been left in as it was used to test the various methods
    // during development.
    private static void testMethod(){

        String var;
        String convertedTime;
        var = "14:15";
        System.out.println("Converting " + var + " to standard time.");
        convertedTime = miliToStandard(var);
        System.out.println(convertedTime);

        var = "8:05:PM";
        System.out.println("Converting " + var + " to Military time.");
        convertedTime = standardToMili(var);
        System.out.println(convertedTime);

    }

    private static int[] convertstringtoInt(String[] splitTime){

        int[] values = new int[2];


        int counter = 0;
        // for each string in our list of strings contained in the variable splitTime
        for (String x:splitTime){

            // converts string into an integer and stores it in the convertedValue variable
            Integer convertedValue = Integer.valueOf(x);

            // adds our integer to our values array variable
            values[counter] = convertedValue;

            // increments counter
            counter ++;
        }

        return values;
    }

    private static boolean sanitizeMili(int[] militaryTime){

        int counter = 0;
        for (int x:militaryTime){
            if (counter == 0 && x > 24){
                throw new IllegalArgumentException();
            }

            if (counter == 1 && x > 60){
                throw new IllegalArgumentException();
            }
            counter ++;
        }
        return true;

    }

    private static String miliToStandard(String militaryTime){

        String amPM = "";

        // Splits the string on the :
        String splitTime[] = militaryTime.split(":", 2);

        // Extracts the minutes out as a string as if we convert them to
        // an int it will lose numbers with a prepended 0 such as "05"
        String minutes = splitTime[1];

        //converts what was supplied from strings into integers
        int[] values = convertstringtoInt(splitTime);


        // makes sure the numbers that the user put in are within the correct range
        sanitizeMili(values);

        // Allows us to build a string
        StringBuilder result = new StringBuilder();


        // converts the hour to standard format and determines if it was AM or PM
        if (values[0] > 12){
            int var = values[0];
            values[0] = var - 12;
            amPM = "PM";
        } else {
            amPM = "AM";
        }

        // Builds our string to return.
        result.append(Integer.toString(values[0]));
        result.append(":");
        result.append(minutes);
        result.append(":");
        result.append(amPM);



        // Returns string out of method
        return result.toString();
    }

    private static String standardToMili(String standardTime){

        // standardTime needs to be in format HH:MM:AM/HH:MM:PM

        String amPM;

        String[] splitTime = standardTime.split(":", 3);

        // determins if what was submitted was AM or PM
        if ( splitTime[2].toLowerCase().equals("am")){
            amPM = "am";
        } else {
            amPM = "pm";
        }


        String[] parsedTime = {splitTime[0]};

        int[] convertedTime = convertstringtoInt(parsedTime);

        //Makes sure that we have passed in the proper format string
        sanitizeMili(convertedTime);

        // if the time was PM we add 12 to the hour.
        if (amPM.equals("pm")){
            int var  = convertedTime[0];
            convertedTime[0] = var + 12;
        }


        // creates object to build a string
        StringBuilder result = new StringBuilder();

        // Builds our string in prep to return
        result.append(convertedTime[0]);
        result.append(":");
        result.append(splitTime[1]);

        // converts StringBuilder to string and returns it out of the method
        return result.toString();

    }

}

