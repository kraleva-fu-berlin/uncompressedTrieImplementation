import javax.swing.tree.TreeNode;

import java.util.ArrayList;

import static jdk.nashorn.internal.objects.Global.print;

public class UncompressedNodeT <V> {
        static final int ALPHABET_SIZE = 26;
        char key;
        V value;
        boolean isendofword;
        UncompressedNodeT<V> parent;
        UncompressedNodeT<V> children[];
        public UncompressedNodeT(){
            this.value=null;
            this.key=' ';
            this.parent=null;
            this.children=(UncompressedNodeT<V>[] )new UncompressedNodeT[ALPHABET_SIZE+1];
            this.isendofword=false;
         }

    private boolean isLeaf(UncompressedNodeT<V> node){
            return isendofword;
    }

    public String display(UncompressedNodeT<V> root,int level,ArrayList myword) {
        String mystring = new String();
        String wholelist = new String();
        int startindex = 0;
        boolean anotherkind=false;
        for (int i = 0; i < ALPHABET_SIZE + 1; i++) {
            if(root.key=='$'){
                if(!myword.isEmpty()) {
                    mystring = String.join(",", myword.toString());
                    wholelist += mystring;
                    startindex = wholelist.length();
                    mystring = new String();
                    myword=new ArrayList();
                    level=mystring.length();
                }
                myword=new ArrayList();
            }
            if (root.children[i] != null) {
                if(root.parent!=null && root.key!='\u0000' && anotherkind){
                    startindex=findstartindex(myword);
                    myword.addAll(myword.subList(startindex,level+startindex));
                    myword.add(root.children[i].key);
                    display(root.children[i], level + 1, myword);
                } else {
                    anotherkind = true;
                    myword.add(root.children[i].key);
                    //System.out.println(myword.toString());
                    display(root.children[i], level + 1, myword);
                }
                wholelist += mystring;
            }
        }
        return formatString(myword.toString());
    }

    private int findstartindex(ArrayList<Character> mylist){
            boolean second = false;
            for(int i = mylist.size()-1;i>-1;i--){
                System.out.println("(" + i + mylist.get(i).toString() + ")");
                if(mylist.get(i)=='$'&& second||(mylist.get(i)=='['&&second)){
                    return i+1;
                } else if (mylist.get(i)=='$'){
                    second=true;
                }
            }
            return 0;
    }

    private String formatString(String mystring){
            String formattedWord = new String();
            for(int i =0;i<mystring.length();i++){
                if(mystring.charAt(i)!=',' && mystring.charAt(i)!='$'&&mystring.charAt(i)!=' '){
                    formattedWord += mystring.charAt(i);
                } else if (mystring.charAt(i)=='$'&&i<mystring.length()-2){
                    formattedWord+=',';
                }
            }
            return formattedWord;
    }

    public void insert(UncompressedNodeT<V> root,String key,V value){
            UncompressedNodeT<V> helper = root;
            for(int level = 0;level<key.length();level++){
                if(helper.children[key.charAt(level)-'a']==null){
                    helper.children[key.charAt(level)-'a'] = new UncompressedNodeT<V>();
                    helper.children[key.charAt(level)-'a'].key=key.charAt(level);
                    helper.children[key.charAt(level)-'a'].parent=helper;
                }
                helper =  helper.children[key.charAt(level)-'a'];
            }
            if(helper.children[ALPHABET_SIZE]==null){
                helper.children[ALPHABET_SIZE]=new UncompressedNodeT<>();
                helper.children[ALPHABET_SIZE].value=value;
                helper.children[ALPHABET_SIZE].parent = helper;
                helper.children[ALPHABET_SIZE].key='$';
                helper.children[ALPHABET_SIZE].isendofword=true;
            }
        }

        public void remove(UncompressedNodeT<V> root, String key, int charAt){
            if(root == null){
                print("Tree is empty");
            } else if(charAt!=key.length()){
                if(!isEmpty(root)){
                    remove(root.children[key.charAt(charAt)-'a'],(String) key,charAt+1);
                    delete(root);
                }else {
                    print("Such word does not exist");
                }
            } else if (charAt==key.length()){
                //System.out.println(root.children[ALPHABET_SIZE]);
                if(root.children[ALPHABET_SIZE]==null){
                    System.out.println("Such word does not exist");
                } else {
                    delete(root.children[ALPHABET_SIZE]);
                    delete(root);
                    root = null;
                    }
                }
            }

        public boolean isEmpty(UncompressedNodeT<V> root){
            for(int i =0;i<ALPHABET_SIZE;i++){
                if(root.children[i]!=null){
                    return false;
                }
            }
            return true;
        }

    public UncompressedNodeT<V> getNode(UncompressedNodeT<V> root,String key){
        for(int i = 0;i<key.length();i++){
            int index = key.charAt(i)-'a';
            if(root.children[index]!=null){
                root = root.children[index];
            } else {
                print("Such element does not exist");
                return null;
            }
        }
        if(root.children[ALPHABET_SIZE]!=null){
            return root.children[ALPHABET_SIZE];
        } else {
            print("Such element does not exist");
            return null;
        }
    }

        public V get(UncompressedNodeT<V> root,String key){
            for(int i = 0;i<key.length();i++){
                int index = key.charAt(i)-'a';
                if(root.children[index]!=null){
                    root = root.children[index];
                } else {
                    print("Such element does not exist");
                    return null;
                }
            }
            if(root.children[ALPHABET_SIZE]!=null){
                return root.children[ALPHABET_SIZE].value;
            } else {
                print("Such element does not exist");
                return null;
            }
        }

       /*SUCCESSOR funktioniert,aber mann kann entweder dieser Ansatz verwenden,oder
die Graph als eine Liste ausgeben und zwar mit allen Elementen,damit ware es super easy,wenn man die Elemente in dem
Array sortiert und damit den Nachfolger findet*/

    public UncompressedNodeT<V> inOrderSuccessor(UncompressedNodeT<V> root,String key) {
            UncompressedNodeT<V> mynode = getNode(root,key);
            if(!isEmpty(mynode)){
                for(int i = 0;i<ALPHABET_SIZE;i++){
                    if(!isEmpty(mynode)){
                        return minValue(mynode.children[i]);
                    }
                }
            }
            mynode = getNode(root,key).parent;
            for(int i = mynode.key-'a'+1;i<ALPHABET_SIZE;i++){
                if(!isEmpty(mynode)){
                    return minValue(mynode.children[i]);
                }
            }

            UncompressedNodeT<V> p = mynode.parent;
            while (p!=null && mynode==p.children[mynode.key-'a']){
                for(int i = mynode.key+1 -'a';i<ALPHABET_SIZE;i++){
                    if(p.children[i]!=null){
                        return minValue(p.children[i]);
                    }
                    if(p.parent==null){
                        break;
                    }
                    mynode=p;
                    p=p.parent;
                }
                for(int i = mynode.key+1 -'a';i<ALPHABET_SIZE;i++) {
                    if (p.children[i] != null) {
                        return minValue(p.children[i]);
                    }
                }
            }
            return null;
        }

        private UncompressedNodeT<V> minValue(UncompressedNodeT<V> node){
            UncompressedNodeT<V> current = node;
            while (!isEmpty(current)){
                for(int i=0;i<ALPHABET_SIZE;i++){
                    if (current.children[i]!=null){
                        current=current.children[i];
                        break;
                    }
                }
            }
            if(current!=node) {
                return current.children[ALPHABET_SIZE];
            } else {
                return current;
            }
        }


    private UncompressedNodeT<V> letzteGemKnoten(UncompressedNodeT<V> root,String key1,String key2){
            for(int i = 0;i<Math.min(key1.length(),key2.length());i++){
                if(key1.charAt(i)==key2.charAt(i)){
                    root = root.children[key1.charAt(i)-'a'];
                } else{
                    break;
                }
            }
            return root;
        }

        private V getModified(UncompressedNodeT<V> root,String key){
            for(int i = 0;i<key.length();i++){
                int index = key.charAt(i)-'a';
                if(root.children[index]!=null){
                    root = root.children[index];
                } else {
                    print("Such element does not exist");
                    return null;
                }
            }
            if(root.children[ALPHABET_SIZE]!=null){
                return root.children[ALPHABET_SIZE].value;
            } else {
                print("Such element does not exist");
                return null;
            }
        }
        private void delete(UncompressedNodeT<V> node){
            if(node.key=='$') {
                node.parent.children[ALPHABET_SIZE] = null;
            }else if ((node.parent!=null && node.parent.key!='\u0000')){
                node.parent.children[node.key-'a'] = null;
            }
            node=null;
            }

    public static void main(String[] args){
        UncompressedNodeT mytrie = new UncompressedNodeT();
        mytrie.insert(mytrie,"kotka",3);
        mytrie.insert(mytrie,"kotak",5);
        mytrie.insert(mytrie,"mase",4);
        mytrie.insert(mytrie,"mordor",4);
        ArrayList mylist = new ArrayList();
        System.out.println(mytrie.display(mytrie,0,mylist));
       // mytrie.remove(mytrie,"kotka",0);
        mylist = new ArrayList();
        System.out.println(mytrie.display(mytrie,0,mylist));
        System.out.println(mytrie.inOrderSuccessor(mytrie,"kotka").value);
    }

}

