
import { useState, useCallback } from 'react';
import { Word } from '@/types/codebreaker';

const API_BASE = 'http://localhost/codebreaker';

export const useCodebreaker = () => {
  const [words, setWords] = useState<Word[]>([]);
  const [message, setMessage] = useState<string>('');
  const [isLoading, setIsLoading] = useState(false);

  const showMessage = useCallback((msg: string) => {
    setMessage(msg);
    setTimeout(() => setMessage(''), 3000);
  }, []);

  const fetchWords = useCallback(async () => {
    try {
      setIsLoading(true);
      const response = await fetch(`${API_BASE}/words`);
      if (response.ok) {
        const data = await response.json();
        setWords(data);
      }
    } catch (error) {
      showMessage('Connection error');
    } finally {
      setIsLoading(false);
    }
  }, [showMessage]);

  const addWord = useCallback(async (word: string) => {
    try {
      setIsLoading(true);
      const response = await fetch(`${API_BASE}/words`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ word: word.toUpperCase() }),
      });

      if (response.ok) {
        showMessage('Word added successfully');
        await fetchWords();
      } else {
        showMessage('Word not valid');
      }
    } catch (error) {
      showMessage('Connection error');
    } finally {
      setIsLoading(false);
    }
  }, [fetchWords, showMessage]);

  const selectWord = useCallback(async (word: string, hits: number) => {
    try {
      setIsLoading(true);
      const response = await fetch(`${API_BASE}/select`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ word, hits }),
      });

      if (response.ok) {
        showMessage(`Word selected: ${hits} hits`);
        await fetchWords();
      } else {
        showMessage('Word not found');
      }
    } catch (error) {
      showMessage('Connection error');
    } finally {
      setIsLoading(false);
    }
  }, [fetchWords, showMessage]);

  const deleteWord = useCallback(async (word: string) => {
    try {
      setIsLoading(true);
      const response = await fetch(`${API_BASE}/words/${word}`, {
        method: 'DELETE',
      });

      if (response.ok) {
        showMessage('Word deleted');
        await fetchWords();
      } else {
        showMessage('Word not found');
      }
    } catch (error) {
      showMessage('Connection error');
    } finally {
      setIsLoading(false);
    }
  }, [fetchWords, showMessage]);

  const resetGame = useCallback(async () => {
    try {
      setIsLoading(true);
      const response = await fetch(`${API_BASE}/reset`, {
        method: 'POST',
      });

      if (response.ok) {
        showMessage('Game reset successfully');
        setWords([]);
      } else {
        showMessage('Reset failed');
      }
    } catch (error) {
      showMessage('Connection error');
    } finally {
      setIsLoading(false);
    }
  }, [showMessage]);

  return {
    words,
    message,
    isLoading,
    addWord,
    selectWord,
    deleteWord,
    resetGame,
    fetchWords,
  };
};
