package com.example.First_Java_CRUD_Ominakavsar_74026;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;

@Route
public class MainView extends VerticalLayout {

    private final CustomerRepository repo;
    private final CustomerEditor editor;
    private final Grid<Customer> grid = new Grid<>(Customer.class);
    private final TextField filter = new TextField();
    private final Button addNewBtn = new Button("New customer", VaadinIcon.PLUS.create());

    public MainView(CustomerRepository repo, CustomerEditor editor) {
        this.repo = repo;
        this.editor = editor;

        add(new HorizontalLayout(filter, addNewBtn), grid, editor);

        grid.setHeight("300px");
        grid.setColumns("id", "firstName", "lastName");
        filter.setPlaceholder("Filter by last name");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> editor.editCustomer(e.getValue()));
        addNewBtn.addClickListener(e -> editor.editCustomer(new Customer("", "")));
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCustomers(filter.getValue());
        });
        listCustomers(null);
    }

    private void listCustomers(String text) {
        if (StringUtils.hasText(text)) {
            grid.setItems(repo.findByLastNameStartsWithIgnoreCase(text));
        } else {
            grid.setItems(repo.findAll());
        }
    }
}
