/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010 Red Hat Inc. and/or its affiliates and other
 * contributors as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.quickstarts.hibernate_search.temp.infinispan;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.infinispan.Cache;
import org.infinispan.lucene.InfinispanDirectory;
import org.infinispan.remoting.transport.Address;
import org.jboss.as.quickstarts.hibernate_search.model.data.FeedEntry;

/**
 * InfinispanFeedService is a console hello-world application to show the Lucene
 * clustering capabilities.
 * This class parses the user input and drives the actions implemented in FeedActions.
 * <p/>
 * As always when running JGroups to run a demo cluster of multiple applications running
 * on the same host, set these JVM options:
 * -Djava.net.preferIPv4Stack=true -Djgroups.bind_addr=127.0.0.1
 *
 * @author Sanne Grinovero
 * @since 4.0
 */
public class InfinispanFeedService{

    private final FeedActions actions;

    public InfinispanFeedService(InfinispanDirectory infinispanDirectory, Cache<?, ?> cache) {
        actions = new FeedActions(infinispanDirectory, cache);
    }



    public void doQuery(String queryLine) {
        System.out.println("InfinispanFeedService.doQuery" + queryLine);
        Query query = null;
        try {
            query = actions.parseQuery(queryLine);
        } catch (ParseException e) {
            System.out.println("Wrong syntax in query: " + e.getMessage());
            System.out.println("type it again: ");
        }
        List<String> listMatches = actions.listStoredValuesMatchingQuery(query);
        printResult(listMatches);
    }

    public void insertNewFeedEntry(FeedEntry feedEntry) {
        System.out.println("Enter string as new feedEntry:");
        actions.addNewDocument(feedEntry);
    }

    private void listAllDocuments() {
        List<String> listMatches = actions.listAllDocuments();
        printResult(listMatches);
    }

    private void listMembers() {
        List<Address> members = actions.listAllMembers();
        System.out.println("\tmembers:\t" + members);
    }

    private void showOptions() {
        System.out.println(
                "Options:\n" +
                        "\t[1] List cluster members\n" +
                        "\t[2] List all documents in index\n" +
                        "\t[3] insert new text\n" +
                        "\t[4] enter a query\n" +
                        "\t[5] quit");
    }

    private void printResult(List<String> storedValues) {
        System.out.println("Matching documents:\n");
        if (storedValues.isEmpty()) {
            System.out.println("\tNo documents found.");
        } else {
            int i = 0;
            for (String value : storedValues) {
                System.out.println(++i + "\t\"" + value + "\"");
            }
        }
    }

    /*@Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            showOptions();
            boolean warned = false;
            while (!scanner.hasNextInt()) {
                if (!warned) {
                    System.out.println("Invalid option, try again:");
                    warned = true;
                }
                scanner.nextLine();
            }
            int result = scanner.nextInt();
            try {
                switch (result) {
                    case 1:
                        listMembers();
                        break;
                    case 2:
                        listAllDocuments();
                        break;
                    case 3:
                        insertNewFeedEntry(scanner);
                        break;
                    case 4:
                        doQuery(scanner);
                        break;
                    case 5:
                        System.out.println("Quit.");
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
                System.out.println("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

}