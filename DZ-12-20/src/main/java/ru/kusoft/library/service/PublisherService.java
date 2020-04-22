package ru.kusoft.library.service;

import ru.kusoft.library.domain.Publisher;

public interface PublisherService {

    void showPublishers();

    void deletePublisherById(Long id);

    void addNewPublisher();

    Publisher getPublisher();
}
