package fa.nfa;

import fa.State;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class NFA implements NFAInterface {

    private LinkedHashSet<Character> sigma;
    private NFAState startState;
    private LinkedHashSet<NFAState> finalState;
    private LinkedHashSet<NFAState> states;
    private HashMap<String, HashMap<Character, String>> delta;

    public NFA (){
        sigma = new LinkedHashSet<Character>();
        startState = new NFAState();
        finalState = new LinkedHashSet<NFAState>();
        states = new LinkedHashSet<NFAState>();
        delta = new HashMap<String, HashMap<Character, String>>();
    }
    @Override
    public boolean addState(String name) {
        // creates array from the linked hash set, and loops to check the states to prevent duplicates
        Object stateArray[] = states.toArray();
        for(int i = 0; i < stateArray.length; i++){
            if(((NFAState) stateArray[i]).getName().equals(name)){
                return false;
            }
        }
        // adds state if it doesn't exist
        NFAState newState = new NFAState(name);
        states.add(newState);
        return true;
    }

    @Override
    public boolean setFinal(String name) {
        // creates array from the linked hash set, and loops to check the state exists in states
        Object stateArray[] = states.toArray();
        for(int i = 0; i < stateArray.length; i++){
            if(((NFAState) stateArray[i]).getName().equals(name)){
                NFAState newState = new NFAState(name);
                // adds state to finalStates if it is an existing state
                finalState.add(newState);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setStart(String name) {
        // creates array from the linked hash set, and loops to check the state exists in states
        Object stateArray[] = states.toArray();
        for(int i = 0; i < stateArray.length; i++){
            if(((NFAState) stateArray[i]).getName().equals(name)){
                NFAState newState = new NFAState(name);
                // sets the startState if it exists in the set
                startState = newState;
                return true;
            }
        }
        return false;
    }

    @Override
    public void addSigma(char symbol) {
        // checks if the symbol already exists in sigma. Returns if it does to prevent duplicates
        if(sigma.contains(symbol) || symbol == 'e'){
            return;
        }
        sigma.add((Character)symbol);
    }

    @Override
    public boolean accepts(String s) {
        return false;
    }

    @Override
    public Set<Character> getSigma() {
        return sigma;
    }

    @Override
    //return NFAState
    public NFAState getState(String name) {
        // creates array from the linked hash set, and loops to check the states and ensure the state we want is in the set.
        Object stateArray[] = states.toArray();
        for(int i = 0; i < stateArray.length; i++){
            if(((NFAState) stateArray[i]).getName().equals(name)){
                // creates new state and returns it since you can't simply get objects from a set
                //NFAState state = new NFAState(name);
                NFAState state = (NFAState) stateArray[i];
                return state;
            }
        }
        return null;
    }

    @Override
    public boolean isFinal(String name) {
        // creates array from the linked hash set, and loops to check the finalStates set and passes if it's in the set
        Object stateArray[] = finalState.toArray();
        for(int i = 0; i < stateArray.length; i++){
            if(((NFAState) stateArray[i]).getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isStart(String name) {
        // returns true if param is the start state
        if(startState.getName().equals(name)){
            return true;
        }
        return false;
    }

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return null;
    }

    @Override
    public Set<NFAState> eClosure(NFAState s) {
        return null;
    }

    @Override
    public int maxCopies(String s) {
        return 0;
    }

    @Override
    public boolean addTransition(String fromState, Set<String> toStates, char onSymb) {
        boolean fromStateBool = false;
        boolean toStateBool = false;
        boolean onSymbBool = false;
        Object toStateArray[] = toStates.toArray();
        Object stateArray[] = states.toArray();
        // makes sure the fromState is in the states set
        for(int i = 0; i < stateArray.length; i++){

            if(((NFAState) stateArray[i]).getName().equals(fromState)){
                fromStateBool = true;
            }
        }
        // makes sure the toState is in the states set
        for(int i = 0; i < stateArray.length; i++){
            for(int j = 0; j < toStateArray.length; j++) {
                if (((NFAState) stateArray[i]).getName().equals(toStateArray[j])) {
                    toStateBool = true;
                }
            }
        }
        // makes sure the onSymb is in the sigma set
        if(sigma.contains(onSymb) || onSymb == 'e'){
            onSymbBool = true;
        }
        if(fromStateBool && toStateBool && onSymbBool == true){      // if all are in their respective sets, updates delta using a hashmap who's key is the fromState
            delta.putIfAbsent(fromState, new HashMap<>());           // and who's value is another hashmap. That second hashmap uses the symbol as a key and toState as the value.
            for(int i = 0; i < toStateArray.length; i++){
                delta.get(fromState).put(onSymb, (String)toStateArray[i]);
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean isDFA() {
        return false;
    }
}
