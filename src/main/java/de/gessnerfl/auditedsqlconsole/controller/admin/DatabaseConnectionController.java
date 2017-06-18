package de.gessnerfl.auditedsqlconsole.controller.admin;

import de.gessnerfl.auditedsqlconsole.domain.database.connection.model.DatabaseConnection;
import de.gessnerfl.auditedsqlconsole.domain.database.connection.repository.DatabaseServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class DatabaseConnectionController {

    public static final String BASE_PATH = "/admin/database";
    public static final String DATABASE_CONNECTION_LIST_VIEW = "admin/database-connection-list";
    public static final String DATABASE_CONNECTION_EDIT_VIEW = "admin/database-edit-form";

    private final DatabaseServerRepository databaseServerRepository;

    @Autowired
    public DatabaseConnectionController(DatabaseServerRepository databaseServerRepository) {
        this.databaseServerRepository = databaseServerRepository;
    }

    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public ModelAndView list(@RequestParam( value = "page", defaultValue = "0") int page, @RequestParam( value = "size", defaultValue = "25") int size){
        final Pageable pageable = new PageRequest(page, size);
        final Page<DatabaseConnection> servers = databaseServerRepository.findAll(pageable);
        return new ModelAndView(DATABASE_CONNECTION_LIST_VIEW, "page", servers);
    }

    @RequestMapping(value = BASE_PATH+"/create", method = RequestMethod.GET)
    public ModelAndView showCreateView(){
        return new ModelAndView(DATABASE_CONNECTION_EDIT_VIEW, "model", new DatabaseConnection());
    }

    @RequestMapping(value = BASE_PATH+"/create", method = RequestMethod.POST)
    public String create(@ModelAttribute DatabaseConnection model){
        model.setId(UUID.randomUUID().toString());
        databaseServerRepository.save(model);
        return "redirect:"+BASE_PATH;
    }

    @RequestMapping(value = BASE_PATH+"/{id}", method = RequestMethod.GET)
    public ModelAndView showEditView(@PathVariable("id") String id){
        DatabaseConnection model = databaseServerRepository.findOne(id);
        return new ModelAndView(DATABASE_CONNECTION_EDIT_VIEW, "model", model);
    }

    @RequestMapping(value = BASE_PATH+"/{id}", method = RequestMethod.POST)
    public String edit(@PathVariable("id") String id, @ModelAttribute DatabaseConnection model){
        if(!id.equals(model.getId())){
            //FIXME error handling
        }
        databaseServerRepository.save(model);
        return "redirect:"+BASE_PATH;
    }

}
