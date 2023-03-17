package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.repository.NoteMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int createNote(Note note) {
        return noteMapper.insertNote(note);
    }

    public boolean updateNote(Note note) {
        return noteMapper.updateNote(note);
    }

    public Note getNote(int noteId) {
        return noteMapper.findNote(noteId);
    }

    public List<Note> getNotes(int userId) {
        return noteMapper.findNotes(userId);
    }

    public void deleteNote(int noteId) {
        noteMapper.deleteNote(noteId);
    }
}
