package com.mci.ticketpilot.views.lists;

import com.mci.ticketpilot.data.entity.Project;
import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.entity.Users;
import com.mci.ticketpilot.data.service.PilotService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class ProjectForm extends FormLayout {
    TextField projectName = new TextField("Project");
    ComboBox<Users> projectManager = new ComboBox<>("Project Manager");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Project> binder = new BeanValidationBinder<>(Project.class);

    public ProjectForm(List<Project> projects, PilotService service) {

        addClassName("project-form");
        binder.forField(projectManager).bind(Project::getManager, Project::setManager);
        binder.bindInstanceFields(this);

        List<Users> users = service.findAllUsers();
        projectManager.setClearButtonVisible(true);
        projectManager.setPrefixComponent(VaadinIcon.SEARCH.create());
        projectManager.setItems(users);
        projectManager.setItemLabelGenerator(user -> user.getFirstName() + " " + user.getLastName());

        add(projectName,
                projectManager,
                createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            Project project = binder.getBean();
            if (project.getManager() == null) {
                Notification.show("Please select a project manager", 2000, Notification.Position.MIDDLE);
            } else {
                fireEvent(new ProjectForm.SaveEvent(this, project));
            }
        }
    }


    public void setProject(Project project) {
        binder.setBean(project);
    }

    // Events
    public static abstract class ProjectFormEvent extends ComponentEvent<ProjectForm> {
        private Project project;

        protected ProjectFormEvent(ProjectForm source, Project project) {
            super(source, false);
            this.project = project;
        }

        public Project getProject() {
            return project;
        }
    }

    public static class SaveEvent extends ProjectFormEvent {
        SaveEvent(ProjectForm source, Project project) {
            super(source, project);
        }
    }

    public static class DeleteEvent extends ProjectFormEvent {
        DeleteEvent(ProjectForm source, Project project) {
            super(source, project);
        }

    }

    public static class CloseEvent extends ProjectFormEvent {
        CloseEvent(ProjectForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

}