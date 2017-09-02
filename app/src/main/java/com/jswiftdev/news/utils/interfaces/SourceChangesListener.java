package com.jswiftdev.news.utils.interfaces;

/**
 * Created by james on 9/2/17.
 * provide callbacks for clicks and selections on tabs from {@link com.jswiftdev.news.MainActivity}
 */

public interface SourceChangesListener {
    /**
     * called once the a different source has been selected
     *
     * @param newSource string referring to from {@link com.jswiftdev.news.models.Source#id}
     */
    void OnSourceChanged(String newSource);
}
