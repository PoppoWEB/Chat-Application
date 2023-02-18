package function.comp;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class JUsefulList<T> {
    private DefaultListModel<T> model = new DefaultListModel<>();
    private JList<T> list = new JList<>(model);

    public JUsefulList() {
        super();
    }

    public JUsefulList(JList<T> list) {
        this.list = list;
    }

    public void add(T element) {
        model.addElement( element );
    }

    public void remove(int i) {
        model.remove(i);
    }

    public void remove(T element) {
        model.removeElement(element);
    }

    public int getSize() {
        return model.getSize();
    }

    public T get(int i) {
        return model.getElementAt(i);
    }

    public JList<T> getList() {
        return list;
    }
}
