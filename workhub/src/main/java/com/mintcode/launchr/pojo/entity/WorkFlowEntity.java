package com.mintcode.launchr.pojo.entity;

import java.util.List;

/**
 * Created by JulyYu on 2016/4/13.
 */
public class WorkFlowEntity {

    private String id;
    private Workflow Workflow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WorkFlowEntity.Workflow getWorkflow() {
        return Workflow;
    }

    public void setWorkflow(WorkFlowEntity.Workflow workflow) {
        Workflow = workflow;
    }

    public class Workflow{

        private String WorkflowType;
        private List<State> States;
        private List<Trigger> Triggers;
        private List<StateConfig> StateConfigs;
        public String getWorkflowType() {
            return WorkflowType;
        }

        public void setWorkflowType(String workflowType) {
            WorkflowType = workflowType;
        }

        public List<State> getStates() {
            return States;
        }

        public void setStates(List<State> states) {
            States = states;
        }

        public List<Trigger> getTriggers() {
            return Triggers;
        }

        public void setTriggers(List<Trigger> triggers) {
            Triggers = triggers;
        }

        public List<StateConfig> getStateConfigs() {
            return StateConfigs;
        }

        public void setStateConfigs(List<StateConfig> stateConfigs) {
            StateConfigs = stateConfigs;
        }
    }


    public class State{
        private String DisplayName;
        private String Name;
        private String Participants;
        private String Countersign;
        private String Stage;
        private String Department;
        private int Position;
        private int left;
        private int top;
        private boolean visited;

        public boolean isVisited() {
            return visited;
        }

        public void setVisited(boolean visited) {
            this.visited = visited;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getParticipants() {
            return Participants;
        }

        public void setParticipants(String participants) {
            Participants = participants;
        }

        public String getDisplayName() {
            return DisplayName;
        }

        public void setDisplayName(String displayName) {
            DisplayName = displayName;
        }

        public String getCountersign() {
            return Countersign;
        }

        public void setCountersign(String countersign) {
            Countersign = countersign;
        }

        public String getStage() {
            return Stage;
        }

        public void setStage(String stage) {
            Stage = stage;
        }

        public String getDepartment() {
            return Department;
        }

        public void setDepartment(String department) {
            Department = department;
        }

        public int getPosition() {
            return Position;
        }

        public void setPosition(int position) {
            Position = position;
        }

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }
    }
    public class Trigger{
        private String DisplayName;
        private String Name;

        public String getDisplayName() {
            return DisplayName;
        }

        public void setDisplayName(String displayName) {
            DisplayName = displayName;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }
    }

    public class StateConfig{
        private String State;
        private String TargetState;
        private String Trigger;

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }

        public String getTargetState() {
            return TargetState;
        }

        public void setTargetState(String targetState) {
            TargetState = targetState;
        }

        public String getTrigger() {
            return Trigger;
        }

        public void setTrigger(String trigger) {
            Trigger = trigger;
        }
    }
}
