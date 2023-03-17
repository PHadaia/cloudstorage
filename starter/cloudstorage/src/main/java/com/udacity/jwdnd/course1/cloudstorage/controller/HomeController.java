package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final NoteService noteService;
    private final UserService userService;

    public HomeController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping()
    public String homeView(@ModelAttribute("note") Note note) {
        return "home";
    }

    @PostMapping(params="action=save")
    public String addNote(@ModelAttribute("note") Note note, @ModelAttribute("notes") List<Note> notes, Principal principal) {
        note.setUserId(userService.getUserByUsername(principal.getName()).getUserId());
        noteService.createNote(note);
        notes.add(note);
        return "home";
    }

    @PostMapping(params="action=edit")
    public String editNote(@ModelAttribute("note") Note note, @ModelAttribute("notes") List<Note> notes) {
        for(Note n : notes) {
            if(n.getNoteId().equals(note.getNoteId())) {
                n.setNoteTitle(note.getNoteTitle());
                n.setNoteDescription(note.getNoteDescription());
                noteService.updateNote(n);
            }
        }
        return "home";
    }

    @PostMapping(params="action=delete")
    public String deleteNote(@ModelAttribute("note") Note note, @ModelAttribute("notes") List<Note> notes) {
        notes.removeIf(n -> Objects.equals(n.getNoteId(), note.getNoteId()));
        noteService.deleteNote(note.getNoteId());
        return "home";
    }

    @ModelAttribute("notes")
    public List<Note> allNotes(@ModelAttribute("note") Note note, Principal principal) {
        return noteService.getNotes(userService.getUserByUsername(principal.getName()).getUserId());
    }
}
