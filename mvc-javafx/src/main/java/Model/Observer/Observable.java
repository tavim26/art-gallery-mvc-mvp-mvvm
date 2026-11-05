package Model.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable
{
    protected List<Observer> observersList = new ArrayList<>();

    public void addObserver(Observer obs)
    {
        observersList.add(0, obs);
        notifyObservers();
    }

    public void removeObserver(Observer obs)
    {
        observersList.remove(obs);
        notifyObservers();
    }

    public void notifyObservers()
    {
        for (Observer obs : observersList)
        {
            obs.update(this);
        }
    }

    public List<Observer> getObserversList()
    {
        return observersList;
    }

    public void setObserversList(List<Observer> observersList)
    {
        this.observersList = observersList;
    }
}
