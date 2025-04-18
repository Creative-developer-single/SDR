package com.example.sdr.Core.ProjectManager.Components;

import com.example.sdr.Core.ProjectManager.ProjectManager;
import com.example.sdr.Core.ProjectManager.Components.Tools.ComponentCreator;

public class ComponentManager {
    private ProjectManager manager;

    private ComponentCreator componentCreator;

    public ComponentManager(ProjectManager manager) {
        this.manager = manager;
        this.componentCreator = new ComponentCreator(this);
    }
}
