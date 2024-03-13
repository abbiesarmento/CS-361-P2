package fa.nfa;

import fa.State;

import java.util.*;

public class NFA implements NFAInterface {

    private LinkedHashSet<Character> sigma;
    private NFAState startState;
    private LinkedHashSet<NFAState> finalState;
    private LinkedHashSet<NFAState> states;
    private HashMap<String, HashMap<Character, Set<String>>> delta;

    public NFA (){
        sigma = new LinkedHashSet<Character>();
        startState = new NFAState();
        finalState = new LinkedHashSet<NFAState>();
        states = new LinkedHashSet<NFAState>();
        delta = new HashMap<String, HashMap<Character, Set<String>>>();
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
        // Start from the e-closure of the start state
        Set<NFAState> currentStates = eClosure(startState);

        // Iterate over each character in the string
        for (char c : s.toCharArray()) {
            Set<NFAState> nextStates = new HashSet<>();

            // Move to next states based on the current character
            for (NFAState state : currentStates) {
                Set<NFAState> statesForChar = getToState(state, c);
                if (statesForChar != null) {
                    for (NFAState nextState : statesForChar) {
                        nextStates.addAll(eClosure(nextState)); // Include e-closure of next states
                    }
                }
            }

            // Update current states to the next states
            currentStates = nextStates;
        }

        // Check if any of the current states is a final state
        for (NFAState state : currentStates) {
            if (isFinal(state.getName())) {
                return true; // Accept if any final state is reached
            }
        }

        return false; // Reject if no final state is reached
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
        Set<NFAState> result = new HashSet<>();

        // Assume delta is structured correctly and contains transitions
        if (delta.containsKey(from.getName()) && delta.get(from.getName()).containsKey(onSymb)) {
            Set<String> targetStateNames = delta.get(from.getName()).get(onSymb);
            for (String stateName : targetStateNames) {
                NFAState state = getState(stateName);
                if (state != null) {
                    result.add(state);
                }
            }
        }
        return result;
    }

    @Override
    public Set<NFAState> eClosure(NFAState s) {
        Set<NFAState> closure = new HashSet<>();
        Stack<NFAState> stack = new Stack<>();

        stack.push(s);
        closure.add(s);

        while (!stack.isEmpty()) {
            NFAState currentState = stack.pop();

            // Assuming delta is structured to support NFA: String -> (Character -> Set<String>)
            // And assuming a method to convert state names to NFAState objects exists
            if (delta.containsKey(currentState.getName()) && delta.get(currentState.getName()).containsKey('e')) {
                Set<String> nextStateNames = delta.get(currentState.getName()).get('e');
                for (String nextStateName : nextStateNames) {
                    NFAState nextState = getState(nextStateName); // Convert state name to NFAState
                    if (!closure.contains(nextState)) {
                        closure.add(nextState);
                        stack.push(nextState);
                    }
                }
            }
        }

        return closure;
    }

    @Override
    public int maxCopies(String s) {
        // Start with the initial state and its epsilon closure as the current states.
        Set<NFAState> currentStates = new HashSet<>(eClosure(startState));
        int maxCopies = currentStates.size();

        for (int i = 0; i < s.length(); i++) {
            char symbol = s.charAt(i);
            Set<NFAState> nextStates = new HashSet<>();

            for (NFAState state : currentStates) {
                // For each state, find where you can go with the current symbol
                Set<NFAState> transitions = getToState(state, symbol);
                if (transitions != null) {
                    for (NFAState nextState : transitions) {
                        // Add all states reachable from nextState through epsilon closure
                        nextStates.addAll(eClosure(nextState));
                    }
                }
            }

            // Update current states to be the next set of states
            currentStates = nextStates;
            // Update maxCopies if the number of states in this step is greater
            maxCopies = Math.max(maxCopies, currentStates.size());
        }

        // Return the maximum number of NFA "copies" encountered, interpreted as the max breadth of parallel active states.
        return maxCopies;
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
            delta.get(fromState).put(onSymb, toStates);


            return true;
        }
        return false;
    }

    @Override
    public boolean isDFA() {
        // Check for ε transitions; presence of ε transitions means this is not a DFA
        if (sigma.contains('e')) {
            return false;
        }

        // Check each state's transitions for each symbol in the alphabet
        for (String fromState : delta.keySet()) {
            HashMap<Character, Set<String>> transitions = delta.get(fromState);
            LinkedHashSet<Character> seenSymbols = new LinkedHashSet<>();

            // Iterate over transitions from this state
            for (Map.Entry<Character, Set<String>> entry : transitions.entrySet()) {
                char symbol = entry.getKey();

                // If we encounter a symbol more than once or an ε transition, it's not a DFA
                if (!seenSymbols.add(symbol)) {
                    return false;
                }
            }

            // Additionally, ensure there's a transition for each symbol in the alphabet for each state
            if (seenSymbols.size() != sigma.size()) {
                return false;
            }
        }

        // If no ε transitions and exactly one transition per symbol per state, it's a DFA
        return true;
    }
}
