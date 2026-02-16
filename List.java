/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
	
    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }
    
    /** Returns the number of elements in this list. */
    public int getSize() {
 	      return size;
    }

    /** Returns the CharData of the first element in this list. */
    public CharData getFirst() {
        // Your code goes here
        if (null == first) {
            return null;
        }
        return first.cp;
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        Node newNode = new Node(new CharData(chr));
        newNode.next = this.first;
        this.first = newNode;
        this.size++;
        // Your code goes here
    }
    
    /** GIVE Textual representation of this list. */
    public String toString() {
        if (0 == this.size) {
            return "()";
        }
        StringBuilder starterBuilder = new StringBuilder("(");
        Node currentNode = this.first;
        while (null != currentNode) {
            starterBuilder.append(currentNode.toString());
            if (currentNode.next != null) {
                starterBuilder.append(" ");
            }
            currentNode = currentNode.next;
        }
        // Your code goes here
        starterBuilder.append(")");
        return starterBuilder.toString();
    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
        Node currentNode = this.first;
        for (int i = 0; i < this.getSize(); i++) {
            if (null != currentNode.cp && currentNode.cp.equals(chr)) {
                return i;
            }
            currentNode = currentNode.next;
        }
        // Your code goes here
        return -1;
    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
        Node currentNode = this.first;
        while (null != currentNode) {
            if ((null != currentNode.cp) && currentNode.cp.equals(chr)) {
                currentNode.cp.count++;
                return;
            }
            currentNode = currentNode.next;
        }
        this.addFirst(chr);
        // Your code goes here
    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
        Node previousNode = null;
        Node currentNode = this.first;
        while (null != currentNode) {
            if ((null != currentNode.cp) && currentNode.cp.equals(chr)) {
                // In case we want to delete the first node, we want to make the 2nd node the first instead.
                if (null == previousNode) {
                    this.first = this.first.next;
                }
                else {
                    // In case we want to delete a node which isn't the first, just "skip" the node we want to delete.
                    previousNode.next = currentNode.next;
                }
                size--;
                return true;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
        // Your code goes here
        return false;
    }

    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        if (0 > index || index >= this.getSize()) {
            throw new IndexOutOfBoundsException();
        }
        Node currentNode =  this.first;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.next;
        }
        return currentNode.cp;
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
	    CharData[] arr = new CharData[size];
	    Node current = first;
	    int i = 0;
        while (current != null) {
    	    arr[i++]  = current.cp;
    	    current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) {
	    // If the list is empty, there is nothing to iterate   
	    if (size == 0) return null;
	    // Gets the element in position index of this list
	    Node current = first;
	    int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        // Returns an iterator that starts in that element
	    return new ListIterator(current);
    }
}