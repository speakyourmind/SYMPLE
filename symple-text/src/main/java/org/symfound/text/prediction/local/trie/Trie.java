/*
 * Copyright (C) 2014 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.symfound.text.prediction.local.trie;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Javed Gangjee
 */
public class Trie {

///// ONLY USED FOR EDIT DISTANCE CHECKS. SHOULDN'T BE NECESSARY? 

    /**
     *
     */
    public ArrayList<String> corpus;

    private final TrieNode _rootNode;
    //Starts at root
    private TrieNode _currNode;

    //Mostly used for testing
    private int _size;

    /**
     *
     */
    public Trie() {
        _rootNode = new TrieNode(' ');
        _rootNode.setRoot();
        _currNode = _rootNode;
        _size = 0;

//////	Store words added so don't have to do traversal of trie every time
        corpus = new ArrayList<>();
    }

    /**
     * Returns true if specified string is contained in the trie. Uses search()
     * method, and if the rootNode is returned, then the string was not found.
     *
     * @param prefix
     * @return
     */
    public boolean contains(String prefix) {
        _currNode = _rootNode;	//reset _currNode reference
        return !search(prefix).isRoot();
    }

    /**
     * Like contains, but only returns true if word string is contained in trie
     * AND is flagged as a word. If the node returned by search is not flagged,
     * even though a non-root node is returned, this method returns false.
     *
     * @param word
     * @return
     */
    public boolean containsWord(String word) {
        _currNode = _rootNode;
        TrieNode node = search(word);
        if (!node.isRoot()) {
            if (node.isFlagged()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Public version of method findDescendants with unique signature. Takes in
     * a string for findDescendants instead of the TrieNode, because searc() is
     * private, so this is the publicly-visible forwarder.
     *
     * @param string
     * @param matches
     */
    public void findDescendants(String string, List<String> matches) {
        //only return results if string is contained in trie
        if (contains(string)) {
            findDescendants(search(string), matches);
        }
    }

    /**
     * Finds all word descendants of prefix. Returns every word in Trie
     * containing prefix. Takes in a TrieNode as starting node. Since search(),
     * which returns the TrieNode, is a private method, there is a public
     * version of findDescendants with another signature that takes in a String
     * instead of a TrieNode.
     */
    private void findDescendants(TrieNode node, List<String> matches) {
        //exact match, prefix is already a word
        if (node.isFlagged()) {
            matches.add(node.getString());
        }
        if (node.hasChildren()) {
            node.children().values().forEach((child) -> {
                findDescendants(child, matches);
            });
        }
    }

    /**
     * Alternate search with method signature that has only one parameter, no
     * (String sofar) parameter. Because the other search method's two
     * parameters, search(String, String), are needed for recursion, so this
     * signature simplifies the method call.
     *
     * @param prefix
     * @return
     */
    public TrieNode search(String prefix) {
        _currNode = _rootNode;	//reset _currNode reference
        return search(prefix, prefix);
    }

    /**
     * Returns last node in string that equals prefixToMatch. The node returned
     * should contain the last character of prefix. Uses sort of depth-first
     * traversal until string matches. If prefix string not found in Trie, the
     * _rootNode is returned. This recursive method is private because it's
     * signature contains the String sofar. Other method with simpler signature
     * is public.
     */
    private TrieNode search(String sofar, String prefixToMatch) {
        //just in case, convert to lower case
        prefixToMatch = prefixToMatch.toLowerCase();
        sofar = sofar.toLowerCase();
        char[] chars = sofar.toCharArray();
        if (sofar.length() > 0) {
            //Get first character of string
            char key = chars[0];
            //Create new character array without first character
            char[] newChars = new char[chars.length - 1];
            for (int i = 1; i < chars.length; i++) {
                newChars[i - 1] = chars[i];
            }
            chars = newChars;
            if (_currNode.children().containsKey(key)) {
                TrieNode next = _currNode.children().get(key);
                _currNode = next;
                //Recursive step: if prefixToMatch still not equal to node's string
                if (!next.getString().equals(prefixToMatch)) {
                    search(new String(chars), prefixToMatch);
                }
            }
        }
        //If String does not match prefixToMatch, return _root and print message
        if (!_currNode.getString().equals(prefixToMatch)) {
//			LOGGER.info("String not found! (Trie.search)");	
            return _rootNode;
        }
        return _currNode;
    }

//////////JUST USE toCharArray METHOD...!?
    /**
     * Static method converting a string to a character array.
     *
     */
    /*	private static char[] stringToChars(String string) {
     char[] chars = new char[string.length()];
     string.getChars(0, string.length(), chars, 0);
     return chars;
     }*/
    /**
     * Adds a string to the Trie. First checks if node's already exist for first
     * characters in string, and creates new nodes as children from there when
     * character string has no more nodes.
     *
     * @param word
     */
    public void addString(String word) {
        //At end of word, set word flag
        if (word.isEmpty()) {
////////			
/*			if (_currNode.getString().equals("helow")) {
             LOGGER.info("_currNode.getString: " + _currNode.getString());
             }*/

            if (!_currNode.getString().isEmpty()) {
                //When whole string is added, flag last node
                _currNode.flagWord(true);
                //Add to corpus List, to search all words in corpus by some EditDistanceGenerator
                corpus.add(_currNode.getString());
            }
        } else {
            char[] origChars = word.toCharArray();
            char c = origChars[0];

////////	JUST USE chars.subSequence(1), WAY EASIER RIGHT?
            //Create new character array without first character
            char[] newChars = new char[origChars.length - 1];
            for (int i = 1; i < origChars.length; i++) {
                newChars[i - 1] = origChars[i];
            }
            /* Make sure recursion is not infinite by checking 
             that char array is decreasing */
            assert newChars.length - 1 == origChars.length;

            //If TrieNode for string character already exists as child
            if (_currNode.children().containsKey(c)) {
                //Update current node to character TrieNode
                _currNode = _currNode.children().get(c);
                //Recursive step, passing n-1 subSequence of char arr
                addString(new String(newChars));
            } //If TrieNode does NOT yet exist
            else {
                //Create new node
                TrieNode node = new TrieNode(c);
                //Add it to children of current node
                _currNode.children().put(c, node);
                node.setParent(_currNode);	//and pass reference in both directions
                //Update reference to current node
                _currNode = node;
                //Each recursive step adds node
                _size += 1;
                //Recursive step, passing n-1 subSequence of char arr
                addString(new String(newChars));
            }
        }
//////	I SHOULD MOVE THIS TO CONDITIONAL AT BEGINNING OF METHOD, WHERE I ADD WORD TO CORPUS??
        //Reset curr node back to root after entire string is added
        _currNode = _rootNode;
    }

    /**
     * Keep track of size in O(1), mostly for testing purposes.
     *
     * @return 
     */
    public int size() {
        return _size;
    }

    /**
     *
     * @return
     */
    public TrieNode getRoot() {
        return _rootNode;
    }
}
