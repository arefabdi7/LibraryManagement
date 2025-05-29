import java.text.*;
import java.util.*;

public class LIB {
    public static void main(String[] args) throws ParseException {
        LibrarySystem.commandGetter();
    }
}

class LibrarySystem {
    public static HashMap<String, User> users = new HashMap<>();
    public static HashMap<String, Library> libraries = new HashMap<>();
    public static HashMap<String, Category> categories = new HashMap<>();
    public static ArrayList<String> search = new ArrayList<>();
    public static ArrayList<BorrowDetails> borrows = new ArrayList<>();

    static {
        User admin = new Admin("admin", "AdminPass");
        users.put("admin", admin);
        Category cat = new Category("null", "null", "null");
        LibrarySystem.categories.put("null", cat);
    }

    public static boolean isAdmin(String ID, String password) {
        if (!users.containsKey(ID)) {
            System.out.println("not-found");
            return false;
        }
        if (!(users.get(ID) instanceof AddUser) || !(users.get(ID) instanceof AddLibCat)) {
            System.out.println("permission-denied");
            return false;
        }
        if (!password.equals("AdminPass")) {
            System.out.println("invalid-pass");
            return false;
        }
        return true;
    }

    public static boolean checkSituation(String managerID, String managerPass, String ID, String library, String category) {
        if (!LibrarySystem.users.containsKey(managerID) || !LibrarySystem.libraries.containsKey(library) || !LibrarySystem.categories.containsKey(category)) {
            System.out.println("not-found");
            return false;
        }
        if (LibrarySystem.libraries.get(library).getResources().containsKey(ID)) {
            System.out.println("duplicate-id");
            return false;
        }
        if (!(LibrarySystem.users.get(managerID) instanceof AddResourcePermission)) {
            System.out.println("permission-denied");
            return false;
        }
        Manager mng = (Manager) LibrarySystem.users.get(managerID);
        if (!mng.getLibID().equals(library)) {
            System.out.println("permission-denied");
            return false;
        }
        if (!mng.getPassword().equals(managerPass)) {
            System.out.println("invalid-pass");
            return false;
        }
        return true;
    }

    public static void commandGetter() throws ParseException {
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        while (!command.equals("finish")) {
            commandCompiler(command);
            command = scanner.nextLine();
        }
    }

    public static void commandCompiler(String command) throws ParseException {
        String[] splitCommand = command.split("[#|]");
        Admin admin = new Admin();
        Manager manager = new Manager();
        Staff staff = new Staff();
        Professor professor = new Professor();
        switch (splitCommand[0]) {
            case "add-library" ->
                    admin.addLibrary(splitCommand[1], splitCommand[2], splitCommand[3], splitCommand[4], splitCommand[5], Integer.parseInt(splitCommand[6]), splitCommand[7]);
            case "add-category" ->
                    admin.addCategory(splitCommand[1], splitCommand[2], splitCommand[3], splitCommand[4], splitCommand[5]);
            case "add-student" ->
                    admin.addStudent(splitCommand[1], splitCommand[2], splitCommand[3], splitCommand[4], splitCommand[5], splitCommand[6], splitCommand[7], splitCommand[8], splitCommand[9]);
            case "add-staff" ->
                    admin.addStaff(splitCommand[1], splitCommand[2], splitCommand[3], splitCommand[4], splitCommand[5], splitCommand[6], splitCommand[7], splitCommand[8], splitCommand[9], splitCommand[10]);
            case "add-manager" ->
                    admin.addManager(splitCommand[1], splitCommand[2], splitCommand[3], splitCommand[4], splitCommand[5], splitCommand[6], splitCommand[7], splitCommand[8], splitCommand[9], splitCommand[10]);
            case "remove-user" -> admin.removeUser(splitCommand[1], splitCommand[2], splitCommand[3]);
            case "add-book" ->
                    manager.addBook(splitCommand[1], splitCommand[2], splitCommand[3], splitCommand[4], splitCommand[5], splitCommand[6], splitCommand[7], Integer.parseInt(splitCommand[8]), splitCommand[9], splitCommand[10]);
            case "add-thesis" ->
                    manager.addThesis(splitCommand[1], splitCommand[2], splitCommand[3], splitCommand[4], splitCommand[5], splitCommand[6], splitCommand[7], splitCommand[8], splitCommand[9]);
            case "add-ganjineh-book" ->
                    manager.addTreasureBook(splitCommand[1], splitCommand[2], splitCommand[3], splitCommand[4], splitCommand[5], splitCommand[6], splitCommand[7], splitCommand[8], splitCommand[9], splitCommand[10]);
            case "add-selling-book" ->
                    manager.addSellingBook(splitCommand[1], splitCommand[2], splitCommand[3], splitCommand[4], splitCommand[5], splitCommand[6], splitCommand[7], Integer.parseInt(splitCommand[8]), Integer.parseInt(splitCommand[9]), Integer.parseInt(splitCommand[10]), splitCommand[11], splitCommand[12]);
            case "remove-resource" ->
                    manager.removeResource(splitCommand[1], splitCommand[2], splitCommand[3], splitCommand[4]);
            case "borrow" ->
                    manager.borrow(splitCommand[1], splitCommand[2], splitCommand[3], splitCommand[4], splitCommand[5], splitCommand[6]);
            case "return" ->
                    manager.returnResource(splitCommand[1], splitCommand[2], splitCommand[3], splitCommand[4], splitCommand[5], splitCommand[6]);
            case "buy" -> professor.buy(splitCommand[1], splitCommand[2], splitCommand[3], splitCommand[4]);
            case "read" ->
                    professor.read(splitCommand[1], splitCommand[2], splitCommand[3], splitCommand[4], splitCommand[5], splitCommand[6]);
            case "add-comment" ->
                    professor.addComment(splitCommand[1], splitCommand[2], splitCommand[3], splitCommand[4], splitCommand[5]);
            case "search" -> admin.search(splitCommand[1]);
            case "search-user" -> staff.searchUser(splitCommand[1], splitCommand[2], splitCommand[3]);
            case "category-report" ->
                    manager.categoryReport(splitCommand[1], splitCommand[2], splitCommand[3], splitCommand[4]);
            case "library-report" -> manager.libraryReport(splitCommand[1], splitCommand[2], splitCommand[3]);
            case "report-passed-deadline" ->
                    manager.reportPassedDeadlines(splitCommand[1], splitCommand[2], splitCommand[3], splitCommand[4], splitCommand[5]);
            case "report-penalties-sum" -> manager.reportPenaltySum(splitCommand[1], splitCommand[2]);
        }
    }
}

class Library {
    private String manager;
    private String ID, name, establishedYear, address;
    private int numberOfDesks;
    private HashMap<String, Resource> resources = new HashMap<>();

    public Library(String ID, String name, String establishedYear, int numberOfDesks, String address) {
        this.ID = ID;
        this.name = name;
        this.establishedYear = establishedYear;
        this.address = address;
        this.numberOfDesks = numberOfDesks;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEstablishedYear() {
        return establishedYear;
    }

    public void setEstablishedYear(String establishedYear) {
        this.establishedYear = establishedYear;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumberOfDesks() {
        return numberOfDesks;
    }

    public void setNumberOfDesks(int numberOfDesks) {
        this.numberOfDesks = numberOfDesks;
    }

    public HashMap<String, Resource> getResources() {
        return resources;
    }

    public void setResources(HashMap<String, Resource> resources) {
        this.resources = resources;
    }


}

class Category {
    private String ID, name, parentID;

    public Category(String ID, String name, String parentID) {
        this.ID = ID;
        this.name = name;
        this.parentID = parentID;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

abstract class Resource {
    private String ID, name, author, library, category;
    private int number;
    private final int constNumber;

    public Resource(String ID, String name, String author, int number, int constNumber, String library, String category) {
        this.ID = ID;
        this.name = name;
        this.author = author;
        this.library = library;
        this.category = category;
        this.number = number;
        this.constNumber = constNumber;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getConstNumber() {
        return constNumber;
    }
}

abstract class Book extends Resource {
    private String publisher, publishYear;

    public Book(String ID, String name, String author, int number, int constNumber, String publisher, String publishYear, String library, String category) {
        super(ID, name, author, number, constNumber, library, category);
        this.publisher = publisher;
        this.publishYear = publishYear;
    }

    public String getPublisher() {
        return publisher;
    }
}

class OrdinaryBook extends Book {
    public OrdinaryBook(String ID, String name, String author, int number, int constNumber, String publisher, String publishYear, String library, String category) {
        super(ID, name, author, number, constNumber, publisher, publishYear, library, category);
    }
}

class TreasureBook extends Book {
    private final String donor;
    private Date returnTime;

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public TreasureBook(String ID, String name, String author, int number, int constNumber, String publisher, String publishYear, String library, String category, String donor) {
        super(ID, name, author, number, constNumber, publisher, publishYear, library, category);
        this.donor = donor;
        this.returnTime = new Date(0);
    }

}

class SellingBook extends Book {
    private final int price, offer;

    public int getPrice() {
        return price;
    }

    public int getOffer() {
        return offer;
    }

    public SellingBook(String ID, String name, String author, int number, int constNumber, String library, String category, String publisher, String publishYear, int price, int offer) {
        super(ID, name, author, number, constNumber, library, category, publisher, publishYear);
        this.price = price;
        this.offer = offer;
    }
}

class Thesis extends Resource {
    String supervisor, defendYear;

    public Thesis(String ID, String name, String author, int number, int constNumber, String supervisor, String defendYear, String library, String category) {
        super(ID, name, author, number, constNumber, library, category);
        this.supervisor = supervisor;
        this.defendYear = defendYear;
    }
}

interface Borrow {
    int who(String ID);

    int what(String sourceID, String library);

    boolean checkBorrow(String ID, String sourceID, String library, Date now);

    void borrow(String ID, String password, String library, String sourceID, String date, String time) throws ParseException;
}

class BorrowDetails {
    String sourceID, library;
    int who, what;
    Date returnTime;

    public BorrowDetails(String sourceID, String library, int who, int what, Date returnTime) {
        this.sourceID = sourceID;
        this.library = library;
        this.who = who;
        this.what = what;
        this.returnTime = returnTime;
    }
}

interface Return {

    long penaltyCalculator(String ID, String sourceID, Date now, int who);

    void returnResource(String ID, String password, String library, String sourceID, String date, String time) throws ParseException;
}

interface Buy {
    public void buy(String ID, String password, String library, String sourceID);
}

interface Read {
    void read(String ID, String password, String library, String sourceID, String date, String time) throws ParseException;
}

interface Comment {
    public int who(String ID);

    public void addComment(String ID, String password, String library, String resourceID, String comment);
}

interface AddUser {
    void addStudent(String adminId, String adminPass, String ID, String password, String name, String lastName, String nationalID, String birthday, String address);

    void addStaff(String adminID, String adminPass, String ID, String password, String name, String lastName, String nationalID, String birthday, String address, String type);

    void addManager(String adminID, String adminPass, String ID, String password, String name, String lastName, String nationalID, String birthday, String address, String libID);
}

interface AddLibCat {
    void addLibrary(String adminID, String adminPass, String ID, String name, String establishedYear, int numberOfDesks, String address);

    void addCategory(String admin, String adminPass, String ID, String name, String parentID);
}

abstract class User implements Search, Borrow, Return {
    private String ID, password, name, lastName, nationalID, birthday, address;
    private long cash;
    ArrayList<BorrowDetails> borrowedResources = new ArrayList<>();

    public User(String ID, String password, String name, String lastName, String nationalID, String birthday, String address) {
        this.ID = ID;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.nationalID = nationalID;
        this.birthday = birthday;
        this.address = address;
        cash = 0;
    }

    public User(String ID, String password) {
        this.ID = ID;
        this.password = password;
    }

    public User() {
    }

    public long getCash() {
        return cash;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setCash(long cash) {
        this.cash = cash;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public int who(String ID) {
        if (LibrarySystem.users.get(ID) instanceof Student)
            return 1;
        if (LibrarySystem.users.get(ID) instanceof Staff)
            return 2;
        return -1;
    }

    @Override
    public int what(String sourceID, String library) {
        if (LibrarySystem.libraries.get(library).getResources().get(sourceID) instanceof OrdinaryBook)
            return 1;
        if (LibrarySystem.libraries.get(library).getResources().get(sourceID) instanceof Thesis)
            return 2;
        return -1;
    }

    @Override
    public boolean checkBorrow(String ID, String sourceID, String library, Date now) {
        for (BorrowDetails br : LibrarySystem.users.get(ID).borrowedResources) {
            if (br.sourceID.equals(sourceID) && br.library.equals(library))
                return true;
            if (br.returnTime.getTime() <= now.getTime())
                return true;
        }
        return false;
    }

    @Override
    public void borrow(String ID, String password, String library, String sourceID, String date, String time) throws ParseException {
        Date now = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(date + " " + time);
        Date returnTime = new Date();
        if (Staff.checkNotFound(ID, password, library, sourceID)) return;
        int who = who(ID);
        int what = what(sourceID, library);
        if ((who == 1 && LibrarySystem.users.get(ID).borrowedResources.size() == 3)) {
            System.out.println("not-allowed");
            return;
        }
        if (who == 2 && LibrarySystem.users.get(ID).borrowedResources.size() == 5) {
            System.out.println("not-allowed");
            return;
        }
        if (LibrarySystem.libraries.get(library).getResources().get(sourceID).getNumber() == 0) {
            System.out.println("not-allowed");
            return;
        }
        if (what == -1) {
            System.out.println("not-allowed");
            return;
        }
        if (checkBorrow(ID, sourceID, library, now)) {
            System.out.println("not-allowed");
            return;
        }
        if (who == 1 && what == 2)
            returnTime.setTime(now.getTime() + (1000 * 60 * 60 * 24 * 7));
        else if (who == 2 && what == 1)
            returnTime.setTime(now.getTime() + (1000 * 60 * 60 * 24 * 14));
        else returnTime.setTime(now.getTime() + (1000 * 60 * 60 * 24 * 10));
        BorrowDetails br = new BorrowDetails(sourceID, library, who, what, returnTime);
        LibrarySystem.libraries.get(library).getResources().get(sourceID).setNumber(LibrarySystem.libraries.get(library).getResources().get(sourceID).getNumber() - 1);
        LibrarySystem.users.get(ID).borrowedResources.add(br);
        LibrarySystem.borrows.add(br);
        System.out.println("success");
    }

    @Override
    public void search(String x) {
        String searchWord = x.toLowerCase();
        for (String lib : LibrarySystem.libraries.keySet()) {
            for (Resource rsc : LibrarySystem.libraries.get(lib).getResources().values()) {
                if (rsc instanceof Book) {
                    Book bk = (Book) rsc;
                    if (bk.getName().toLowerCase().contains(searchWord)
                            || bk.getAuthor().toLowerCase().contains(searchWord)
                            || bk.getPublisher().toLowerCase().contains(searchWord))
                        LibrarySystem.search.add(bk.getID());
                }
                if (rsc instanceof Thesis) {
                    Thesis ths = (Thesis) rsc;
                    if (ths.supervisor.toLowerCase().contains(searchWord)
                            || ths.getName().toLowerCase().contains(searchWord)
                            || ths.getAuthor().toLowerCase().contains(searchWord))
                        LibrarySystem.search.add(ths.getID());
                }
            }
        }
        if (LibrarySystem.search.isEmpty()) {
            System.out.println("not-found");
            return;
        }
        List<String> list = new ArrayList<>(LibrarySystem.search);
        Collections.sort(list);
        for (int i = 0; i < list.size() - 1; i++)
            System.out.print(list.get(i) + "|");
        System.out.println(list.get(list.size() - 1));
        LibrarySystem.search.clear();
    }

    @Override
    public long penaltyCalculator(String ID, String sourceID, Date now, int who) {
        for (BorrowDetails br : LibrarySystem.users.get(ID).borrowedResources) {
            if (br.sourceID.equals(sourceID) && who == 1)
                return ((now.getTime() - br.returnTime.getTime()) / (1000 * 60 * 60)) * 50;
            if (br.sourceID.equals(sourceID) && who == 2)
                return ((now.getTime() - br.returnTime.getTime()) / (1000 * 60 * 60)) * 100;
        }
        return 0;
    }

    @Override

    public void returnResource(String ID, String password, String library, String sourceID, String date, String time) throws ParseException {
        Date now = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(date + " " + time);
        User user = new Manager();
        if (Staff.checkNotFound(ID, password, library, sourceID)) return;
        boolean check = false;
        int count = 0;
        for (BorrowDetails br : LibrarySystem.users.get(ID).borrowedResources) {
            if (br.sourceID.equals(sourceID)) {
                check = true;
                break;
            }
            count++;
        }
        if (!check) {
            System.out.println("not-found");
            return;
        }
        int who = user.who(ID);
        long penalty = penaltyCalculator(ID, sourceID, now, who);
        if (penalty <= 0)
            System.out.println("success");
        else System.out.println(penalty);
        if (penalty > 0)
            LibrarySystem.users.get(ID).setCash(LibrarySystem.users.get(ID).getCash() - penalty);
        LibrarySystem.libraries.get(library).getResources().get(sourceID).setNumber(LibrarySystem.libraries.get(library).getResources().get(sourceID).getNumber() + 1);
        LibrarySystem.users.get(ID).borrowedResources.remove(count);
    }
}

interface Search {
    void search(String searchWord);
}

interface SearchUser {
    void searchUser(String ID, String password, String searchWord);
}

class Admin extends User implements AddUser, AddLibCat {
    public Admin(String ID, String password) {
        super(ID, password);
    }

    public Admin() {

    }

    @Override
    public void addLibrary(String adminID, String adminPass, String ID, String name, String establishedYear, int numberOfDesks, String address) {
        if (LibrarySystem.isAdmin(adminID, adminPass)) {
            if (LibrarySystem.libraries.containsKey(ID)) {
                System.out.println("duplicate-id");
                return;
            }
            Library lib = new Library(ID, name, establishedYear, numberOfDesks, address);
            LibrarySystem.libraries.put(ID, lib);
            System.out.println("success");
        }
    }

    @Override
    public void addCategory(String admin, String adminPass, String ID, String name, String parentID) {
        if (LibrarySystem.isAdmin(admin, adminPass)) {
            if (LibrarySystem.categories.containsKey(ID)) {
                System.out.println("duplicate-id");
                return;
            }
            if (!LibrarySystem.categories.containsKey(parentID)) {
                System.out.println("not-found");
                return;
            }
            Category cat = new Category(ID, name, parentID);
            LibrarySystem.categories.put(ID, cat);
            System.out.println("success");
        }
    }

    @Override
    public void addStudent(String adminId, String adminPass, String ID, String password, String name, String lastName, String nationalID, String birthday, String address) {
        if (LibrarySystem.isAdmin(adminId, adminPass)) {
            if (LibrarySystem.users.containsKey(ID)) {
                System.out.println("duplicate-id");
                return;
            }
            User std = new Student(ID, password, name, lastName, nationalID, birthday, address);
            LibrarySystem.users.put(ID, std);
            System.out.println("success");
        }
    }

    @Override
    public void addStaff(String adminID, String adminPass, String ID, String password, String name, String lastName, String nationalID, String birthday, String address, String type) {
        if (LibrarySystem.isAdmin(adminID, adminPass)) {
            if (LibrarySystem.users.containsKey(ID)) {
                System.out.println("duplicate-id");
                return;
            }
            User user;
            if (type.equals("staff")) user = new Employee(ID, password, name, lastName, nationalID, birthday, address);
            else user = new Professor(ID, password, name, lastName, nationalID, birthday, address);
            LibrarySystem.users.put(ID, user);
            System.out.println("success");
        }
    }

    @Override
    public void addManager(String adminID, String adminPass, String ID, String password, String name, String lastName, String nationalID, String birthday, String address, String libID) {
        if (LibrarySystem.isAdmin(adminID, adminPass)) {
            if (LibrarySystem.users.containsKey(ID)) {
                System.out.println("duplicate-id");
                return;
            }
            if (!LibrarySystem.libraries.containsKey(libID)) {
                System.out.println("not-found");
                return;
            }
            User mng = new Manager(ID, password, name, lastName, nationalID, birthday, address, libID);
            LibrarySystem.users.put(ID, mng);
            LibrarySystem.libraries.get(libID).setManager(ID);
            System.out.println("success");
        }
    }

    public void removeUser(String adminID, String adminPass, String userID) {
        if (LibrarySystem.isAdmin(adminID, adminPass)) {
            if (!LibrarySystem.users.containsKey(userID)) {
                System.out.println("not-found");
                return;
            }
            User user = LibrarySystem.users.get(userID);
            if (!user.borrowedResources.isEmpty()) {
                System.out.println("not-allowed");
                return;
            }
            LibrarySystem.users.remove(userID);
            System.out.println("success");

        }
    }
}

class Student extends User implements Buy, Comment {
    public Student(String ID, String password, String name, String lastName, String nationalID, String birthday, String address) {
        super(ID, password, name, lastName, nationalID, birthday, address);
    }

    @Override
    public void buy(String ID, String password, String library, String sourceID) {
        if (Staff.checkNotFound(ID, password, library, sourceID)) return;
        if (LibrarySystem.users.get(ID) instanceof Manager) {
            System.out.println("permission-denied");
            return;
        }
        if (!(LibrarySystem.libraries.get(library).getResources().get(sourceID) instanceof SellingBook)
                || (LibrarySystem.users.get(ID).getCash() < 0)
                || LibrarySystem.libraries.get(library).getResources().get(sourceID).getNumber() == 0) {
            System.out.println("not-allowed");
            return;
        }
        LibrarySystem.libraries.get(library).getResources().get(sourceID).setNumber(LibrarySystem.libraries.get(library).getResources().get(sourceID).getNumber() - 1);
        System.out.println("success");
    }

    @Override
    public int who(String ID) {
        if (!LibrarySystem.users.containsKey(ID))
            return 0;
        if (LibrarySystem.users.get(ID) instanceof Student || LibrarySystem.users.get(ID) instanceof Professor)
            return 1;
        else
            return -1;
    }

    @Override
    public void addComment(String ID, String password, String library, String resourceID, String comment) {
        if (Staff.checkNotFound(ID, password, library, resourceID)) return;
        if (who(ID) == -1) {
            System.out.println("permission-denied");
            return;
        }
        System.out.println("success");
    }
}

class Staff extends User implements Buy, SearchUser {

    public Staff(String ID, String password, String name, String lastName, String nationalID, String birthday, String address) {
        super(ID, password, name, lastName, nationalID, birthday, address);
    }

    public Staff() {
    }

    @Override
    public void searchUser(String ID, String password, String x) {
        String searchWord = x.toLowerCase();
        if (!LibrarySystem.users.containsKey(ID)) {
            System.out.println("not-found");
            return;
        }
        if (!LibrarySystem.users.get(ID).getPassword().equals(password)) {
            System.out.println("invalid-pass");
            return;
        }
        if ((LibrarySystem.users.get(ID) instanceof Student)) {
            System.out.println("permission-denied");
            return;
        }
        for (User user : LibrarySystem.users.values())
            if (!(user instanceof Admin) && (user.getName().toLowerCase().contains(searchWord) || user.getLastName().toLowerCase().contains(searchWord)))
                LibrarySystem.search.add(user.getID());
        if (LibrarySystem.search.isEmpty()) {
            System.out.println("not-found");
            return;
        }
        List<String> list = new ArrayList<>(LibrarySystem.search);
        Collections.sort(list);
        for (int i = 0; i < list.size() - 1; i++)
            System.out.print(list.get(i) + "|");
        System.out.println(list.get(list.size() - 1));
        LibrarySystem.search.clear();

    }

    @Override
    public void buy(String ID, String password, String library, String sourceID) {
        if (checkNotFound(ID, password, library, sourceID)) return;
        if (LibrarySystem.users.get(ID) instanceof Manager) {
            System.out.println("permission-denied");
            return;
        }
        if (!(LibrarySystem.libraries.get(library).getResources().get(sourceID) instanceof SellingBook)
                || (LibrarySystem.users.get(ID).getCash() < 0)
                || LibrarySystem.libraries.get(library).getResources().get(sourceID).getNumber() == 0) {
            System.out.println("not-allowed");
            return;
        }
        LibrarySystem.libraries.get(library).getResources().get(sourceID).setNumber(LibrarySystem.libraries.get(library).getResources().get(sourceID).getNumber() - 1);
        System.out.println("success");
    }

    public static boolean checkNotFound(String ID, String password, String library, String sourceID) {
        if (Manager.middleman(ID, password)) return true;
        if (!LibrarySystem.libraries.containsKey(library) || !LibrarySystem.libraries.get(library).getResources().containsKey(sourceID)) {
            System.out.println("not-found");
            return true;
        }
        return false;
    }
}

class Employee extends Staff {
    public Employee(String ID, String password, String name, String lastName, String nationalID, String birthday, String address) {
        super(ID, password, name, lastName, nationalID, birthday, address);
    }
}

class Professor extends Staff implements Read, Comment {
    public Professor(String ID, String password, String name, String lastName, String nationalID, String birthday, String address) {
        super(ID, password, name, lastName, nationalID, birthday, address);
    }

    public Professor() {
    }

    @Override
    public void read(String ID, String password, String library, String sourceID, String date, String time) throws ParseException {
        Date now = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(date + " " + time);
        if (Staff.checkNotFound(ID, password, library, sourceID)) return;
        if (!(LibrarySystem.users.get(ID) instanceof Professor)) {
            System.out.println("permission-denied");
            return;
        }
        if (!(LibrarySystem.libraries.get(library).getResources().get(sourceID) instanceof TreasureBook)) {
            System.out.println("not-allowed");
            return;
        }
        TreasureBook tbk = (TreasureBook) LibrarySystem.libraries.get(library).getResources().get(sourceID);
        if (now.getTime() < tbk.getReturnTime().getTime()) {
            System.out.println("not-allowed");
            return;
        }
        Date returnTime = new Date(now.getTime() + (1000 * 60 * 60 * 2));
        tbk.setReturnTime(returnTime);
        System.out.println("success");
    }

    @Override
    public int who(String ID) {
        if (!LibrarySystem.users.containsKey(ID))
            return 0;
        if (LibrarySystem.users.get(ID) instanceof Student || LibrarySystem.users.get(ID) instanceof Professor)
            return 1;
        else
            return -1;
    }

    @Override
    public void addComment(String ID, String password, String library, String resourceID, String comment) {
        if (Staff.checkNotFound(ID, password, library, resourceID)) return;
        if (who(ID) == -1) {
            System.out.println("permission-denied");
            return;
        }
        System.out.println("success");
    }
}

interface AddResourcePermission {
    void addBook(String managerID, String managerPass, String ID, String name, String author, String publisher, String publishYear, int number, String category, String library);

    void addThesis(String managerID, String managerPass, String ID, String name, String author, String supervisor, String defendYear, String category, String library);

    void addTreasureBook(String managerID, String managerPass, String ID, String name, String author, String publisher, String publishYear, String donor, String category, String library);

    void addSellingBook(String managerID, String managerPass, String ID, String name, String author, String publisher, String publishYear, int number, int price, int offer, String category, String library);
}

interface Reports {
    void categoryReport(String ID, String password, String category, String library);

    void libraryReport(String ID, String password, String library);

    void reportPassedDeadlines(String ID, String password, String library, String date, String time) throws ParseException;

    void reportPenaltySum(String ID, String password);
}

class Manager extends User implements AddResourcePermission, Reports {
    private String libID;

    public Manager(String ID, String password, String name, String lastName, String nationalID, String birthday, String address, String libID) {
        super(ID, password, name, lastName, nationalID, birthday, address);
        this.libID = libID;
    }

    public Manager() {

    }

    public String getLibID() {
        return libID;
    }

    @Override
    public void addBook(String managerID, String managerPass, String ID, String name, String author, String publisher, String publishYear, int number, String category, String library) {
        if (LibrarySystem.checkSituation(managerID, managerPass, ID, library, category)) {
            Resource book = new OrdinaryBook(ID, name, author, number, number, publisher, publishYear, library, category);
            LibrarySystem.libraries.get(library).getResources().put(ID, book);
            System.out.println("success");
        }
    }

    @Override
    public void addThesis(String managerID, String managerPass, String ID, String name, String author, String supervisor, String defendYear, String category, String library) {
        if (LibrarySystem.checkSituation(managerID, managerPass, ID, library, category)) {
            Resource thesis = new Thesis(ID, name, author, 1, 1, supervisor, defendYear, library, category);
            LibrarySystem.libraries.get(library).getResources().put(ID, thesis);
            System.out.println("success");
        }
    }

    @Override
    public void addTreasureBook(String managerID, String managerPass, String ID, String name, String author, String publisher, String publishYear, String donor, String category, String library) {
        if (LibrarySystem.checkSituation(managerID, managerPass, ID, library, category)) {
            Resource trBook = new TreasureBook(ID, name, author, 1, 1, publisher, publishYear, library, category, donor);
            LibrarySystem.libraries.get(library).getResources().put(ID, trBook);
            System.out.println("success");
        }
    }

    @Override
    public void addSellingBook(String managerID, String managerPass, String ID, String name, String author, String publisher, String publishYear, int number, int price, int offer, String category, String library) {
        if (LibrarySystem.checkSituation(managerID, managerPass, ID, library, category)) {
            Resource slBook = new SellingBook(ID, name, author, number, number, publisher, publishYear, library, category, price, offer);
            LibrarySystem.libraries.get(library).getResources().put(ID, slBook);
            System.out.println("success");
        }
    }

    public void removeResource(String managerID, String managerPass, String ID, String library) {
        if (!LibrarySystem.users.containsKey(managerID) || !LibrarySystem.libraries.containsKey(library) || !LibrarySystem.libraries.get(library).getResources().containsKey(ID)) {
            System.out.println("not-found");
            return;
        }
        if (!LibrarySystem.users.get(managerID).getPassword().equals(managerPass)) {
            System.out.println("invalid-pass");
            return;
        }
        if (checking(managerID, library)) return;
        if (LibrarySystem.libraries.get(library).getResources().get(ID).getNumber() < LibrarySystem.libraries.get(library).getResources().get(ID).getConstNumber()) {
            System.out.println("not-allowed");
            return;
        }
        LibrarySystem.libraries.get(library).getResources().remove(ID);
        System.out.println("success");
    }

    @Override
    public void categoryReport(String ID, String password, String category, String library) {
        int numOfBooks = 0, numOfThesis = 0, numOfTreasure = 0, numOfSelling = 0;
        ArrayList<String> cat = new ArrayList<>();
        if (!LibrarySystem.users.containsKey(ID)) {
            System.out.println("not-found");
            return;
        }
        if (!LibrarySystem.users.get(ID).getPassword().equals(password)) {
            System.out.println("invalid-pass");
            return;
        }
        if (!LibrarySystem.libraries.containsKey(library) || !LibrarySystem.categories.containsKey(category)) {
            System.out.println("not-found");
            return;
        }
        if (checking(ID, library)) return;
        for (Category ct : LibrarySystem.categories.values()) {
            if (category.equals("null")) {
                cat.add("null");
                break;
            }
            if (ct.getID().equals(category) || ct.getParentID().equals(category))
                cat.add(ct.getID());
        }
        for (Resource rsc : LibrarySystem.libraries.get(library).getResources().values()) {
            for (String ct : cat) {
                if (rsc.getCategory().equals(ct)) {
                    if ((rsc instanceof SellingBook))
                        numOfSelling += LibrarySystem.libraries.get(library).getResources().get(rsc.getID()).getNumber();
                    if ((rsc instanceof OrdinaryBook))
                        numOfBooks += LibrarySystem.libraries.get(library).getResources().get(rsc.getID()).getNumber();
                    if ((rsc instanceof TreasureBook))
                        numOfTreasure++;
                    if ((rsc instanceof Thesis))
                        numOfThesis += LibrarySystem.libraries.get(library).getResources().get(rsc.getID()).getNumber();
                }
            }
        }
        System.out.println(numOfBooks + " " + numOfThesis + " " + numOfTreasure + " " + numOfSelling);
    }

    @Override
    public void libraryReport(String ID, String password, String library) {
        if (anotherCheck(ID, password, library)) return;
        int allBook = 0, allThesis = 0, borrowedBook = 0, borrowedThesis = 0, treasureBook = 0, remainSellingBooks = 0;
        for (Resource rsc : LibrarySystem.libraries.get(library).getResources().values()) {
            if (rsc instanceof Book) {
                if (rsc instanceof OrdinaryBook) {
                    allBook += rsc.getNumber();
                    borrowedBook += rsc.getConstNumber() - rsc.getNumber();
                }
                if (rsc instanceof TreasureBook)
                    treasureBook += rsc.getNumber();
                if (rsc instanceof SellingBook)
                    remainSellingBooks += rsc.getNumber();
            }
            if (rsc instanceof Thesis) {
                allThesis += rsc.getNumber();
                borrowedThesis += rsc.getConstNumber() - rsc.getNumber();
            }
        }
        System.out.println(allBook + " " + allThesis + " " + borrowedBook + " " + borrowedThesis + " " + treasureBook + " " + remainSellingBooks);
    }

    @Override
    public void reportPassedDeadlines(String ID, String password, String library, String date, String time) throws ParseException {
        if (anotherCheck(ID, password, library)) return;
        Date now = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(date + " " + time);
        HashSet<String> deadLines = new HashSet<>();
        for (User user : LibrarySystem.users.values())
            for (BorrowDetails br : user.borrowedResources) {
                if ((now.getTime() > br.returnTime.getTime()) && br.library.equals(library))
                    deadLines.add(br.sourceID);
            }
        if (deadLines.isEmpty()) {
            System.out.println("none");
            return;
        }
        List<String> list = new ArrayList<>(deadLines);
        Collections.sort(list);
        for (int i = 0; i < list.size() - 1; i++)
            System.out.print(list.get(i) + "|");
        System.out.println(list.get(list.size() - 1));
    }

    public boolean anotherCheck(String ID, String password, String library) {
        if (middleman(ID, password)) return true;
        if (!LibrarySystem.libraries.containsKey(library)) {
            System.out.println("not-found");
            return true;
        }
        return checking(ID, library);
    }

    public static boolean middleman(String ID, String password) {
        if (!LibrarySystem.users.containsKey(ID)) {
            System.out.println("not-found");
            return true;
        }
        if (!LibrarySystem.users.get(ID).getPassword().equals(password)) {
            System.out.println("invalid-pass");
            return true;
        }
        return false;
    }

    @Override
    public void reportPenaltySum(String ID, String password) {
        if (LibrarySystem.isAdmin(ID, password)) {
            int sum = 0;
            for (User user : LibrarySystem.users.values())
                sum += user.getCash();
            System.out.println(-sum);
        }

    }

    public boolean checking(String ID, String library) {
        if (!(LibrarySystem.users.get(ID) instanceof Manager)) {
            System.out.println("permission-denied");
            return true;
        }
        Manager mng = (Manager) LibrarySystem.users.get(ID);
        if (!mng.getLibID().equals(library)) {
            System.out.println("permission-denied");
            return true;
        }
        return false;
    }
}
