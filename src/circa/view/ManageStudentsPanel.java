package circa.view;

import circa.controller.AdminController;
import circa.controller.Result;
import circa.model.Student;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class ManageStudentsPanel extends RefreshablePanel {
    private final AdminController controller;
    private final DefaultTableModel model = Widgets.readOnlyModel("Student ID", "Name", "Active Loans");
    private final JTable table = new JTable(model);
    private Student[] rows = new Student[0];

    public ManageStudentsPanel(AdminController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addButton = new JButton("+ Add Student");
        JButton resetButton = new JButton("Reset Password");
        JButton removeButton = new JButton("Remove Student");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(addButton);
        actions.add(resetButton);
        actions.add(removeButton);

        add(new JLabel("Manage Students / Users"), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addStudent());
        resetButton.addActionListener(e -> resetPassword());
        removeButton.addActionListener(e -> removeStudent());
    }

    private Student selectedStudent() {
        int row = table.getSelectedRow();
        if (row < 0) {
            Messages.info(this, "Select a student first.");
            return null;
        }
        return rows[row];
    }

    private void addStudent() {
        RegisterDialog dialog = new RegisterDialog(SwingUtilities.getWindowAncestor(this), "Add Student");
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            Messages.show(this, controller.addStudent(dialog.getStudentId(), dialog.getFullName(), dialog.getPassword()));
            refresh();
        }
    }

    private void resetPassword() {
        Student student = selectedStudent();
        if (student == null) {
            return;
        }
        String password = JOptionPane.showInputDialog(this, "New password for " + student.getId() + ":");
        if (password == null) {
            return;
        }
        Result<Void> result = controller.resetPassword(student.getId(), password);
        Messages.show(this, result);
    }

    private void removeStudent() {
        Student student = selectedStudent();
        if (student == null || !Messages.confirm(this, "Remove " + student.getName() + "?")) {
            return;
        }
        Messages.show(this, controller.removeStudent(student.getId()));
        refresh();
    }

    @Override
    public void refresh() {
        rows = controller.students();
        model.setRowCount(0);
        for (Student student : rows) {
            model.addRow(new Object[]{student.getId(), student.getName(), controller.activeLoanCount(student)});
        }
    }
}
