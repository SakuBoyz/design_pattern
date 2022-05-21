import java.util.*;

class Repo {
    List<Branch> branches = new ArrayList<Branch>();

    public Repo() {
        Branch firstBranch = new Branch("Master");
        branches.add(firstBranch);
    }

    public String NewBranch(String destBranch, String srcBranch) {
        for (var branch : branches) {
            if (branch.branchName == destBranch) {
                return "Branch '" + destBranch + "' already exists.";
            } else if (branch.branchName == srcBranch) {
                Branch newBranch = new Branch(destBranch);
                branches.add(newBranch);
                return "New branch '" + destBranch + "' created successfully from '" + srcBranch + "'";
            } else {
                return "Branch '" + srcBranch + "' does not exists.";
            }
        }
        return "";
    }

    public void AddCommit(String branchName, List<String> listFile) {
        for (Branch branch : branches) {
            if (branch.branchName == branchName) {
                if (branch.commit.size() == 0) { // first commit
                    Commit commit = new Commit(listFile, null, null);
                    branch.commit.add(commit);
                } else {
                    Commit commit = new Commit(listFile, branch.commit.get(branch.commit.size() - 1), null);
                    // update last commit child
                    branch.commit.get(branch.commit.size() - 1).Child = commit;
                    branch.commit.add(commit);
                }
            }
        }
    }

    public void GetHeadFile(String branchName) {
        for (Branch branch : branches) {
            if (branch.branchName == branchName) {
                System.out.println(branch.commit.get(branch.commit.size() - 1).listFile);
            }
        }
    }
}

class Branch {
    String branchName;
    List<Commit> commit = new ArrayList<Commit>();

    public Branch(String _branchName) {
        branchName = _branchName;
    }
}

class Commit {
    int id;
    List<String> listFile;
    Commit Parent;
    Commit Child;

    public Commit(List<String> _listFile, Commit _parent, Commit _child) {
        id = this.hashCode();
        listFile = _listFile;
        Parent = _parent;
        Child = _child;
    }
}

public class CSGit {
    public static void main(String[] args) {
        // Instantiate repo
        Repo repo = new Repo();

        // Custom input elements
        List<String> listFile = new ArrayList<String>();
        listFile.add("file1.txt");
        listFile.add("file2.txt");
        listFile.add("file3.txt");

        List<String> listFile2 = new ArrayList<String>();
        listFile2.add("file4.txt");
        listFile2.add("file5.txt");
        listFile2.add("file6.txt");

        List<String> listFile3 = new ArrayList<String>();
        listFile3.add("file7.txt");
        listFile3.add("file8.txt");
        listFile3.add("file9.txt");

        repo.AddCommit("Master", listFile);
        repo.AddCommit("Master", listFile2);
        repo.GetHeadFile("Master");

        System.out.println(repo.NewBranch("Dev", "Master"));
        repo.AddCommit("Dev", listFile3);
        repo.GetHeadFile("Dev");
    }
}
