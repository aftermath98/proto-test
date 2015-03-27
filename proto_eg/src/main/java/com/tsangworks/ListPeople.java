package com.tsangworks;

import com.tsangworks.auto.AddressBookProtos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;

/**
 *
 */
public class ListPeople {

    private static final Logger logger = LogManager.getLogger();

    // Iterates though all people in the AddressBook and prints info about them.
    static void Print(AddressBookProtos.AddressBook addressBook) {
        for (AddressBookProtos.Person person: addressBook.getPersonList()) {
            logger.debug("Person ID: " + person.getId());
            logger.debug("  Name: " + person.getName());
            if (person.hasEmail()) {
                logger.debug("  E-mail address: " + person.getEmail());
            }

            for (AddressBookProtos.Person.PhoneNumber phoneNumber : person.getPhoneList()) {
                switch (phoneNumber.getType()) {
                    case MOBILE:
                        logger.debug("  Mobile phone #: ");
                        break;
                    case HOME:
                        logger.debug("  Home phone #: ");
                        break;
                    case WORK:
                        logger.debug("  Work phone #: ");
                        break;
                }
                logger.debug(phoneNumber.getNumber());
            }
        }
    }

    // Main function:  Reads the entire address book from a file and prints all
    //   the information inside.
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage:  ListPeople ADDRESS_BOOK_FILE");
            System.exit(-1);
        }

        // Read the existing address book.
        AddressBookProtos.AddressBook addressBook =
                AddressBookProtos.AddressBook.parseFrom(new FileInputStream(args[0]));

        Print(addressBook);
    }
}
