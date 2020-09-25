/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.audio.music.library;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.symfound.audio.music.song.Song;

/**
 *
 * @author Javed Gangjee
 */
public class SongLibrary extends TableView {

    /**
     *
     */
    public SongLibrary() {
        initialize();
    }

    private void initialize() {
        setEditable(true);

        TableColumn titleColumn = new TableColumn("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn artistColumn = new TableColumn("Artist");
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));

        TableColumn albumColumn = new TableColumn("Album");
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));

        TableColumn pathColumn = new TableColumn("Path");
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("path"));
        getColumns().addAll(titleColumn, artistColumn, albumColumn, pathColumn);

        setItems(getSongList());
    }

    /**
     *
     * @param paths
     */
    public void add(List<String> paths) {
        for (String path : paths) {
            Song song = new Song(path);
            if (song.getSongFileAnalyzer().isPlayable()) {
                getSongList().add(song);
            }
        }

    }

    /**
     *
     * @param name
     * @return
     */
    public List<Song> getSongsByArtist(String name) {
        return getItems();
    }

    private ObservableList<Song> songList;

    /**
     *
     * @return
     */
    public ObservableList<Song> getSongList() {
        if (songList == null) {
            songList = FXCollections.observableArrayList();
        }
        return songList;
    }

}
