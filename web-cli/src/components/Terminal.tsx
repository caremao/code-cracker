import React, { useState, useEffect, useRef } from "react";
import { useCodebreaker } from "@/hooks/useCodebreaker";
import { Word } from "@/types/codebreaker";

const Terminal = () => {
  const [inputValue, setInputValue] = useState("");
  const {
    words,
    message,
    isLoading,
    addWord,
    selectWord,
    deleteWord,
    resetGame,
    fetchWords,
  } = useCodebreaker();
  const inputRef = useRef<HTMLInputElement>(null);

  const focusInput = () => inputRef.current?.focus();

  useEffect(() => {
    const interval = setInterval(() => {
      if (document.activeElement !== inputRef.current) {
        inputRef.current?.focus();
      }
    }, 100); // cada 100ms revisa

    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    fetchWords();
    focusInput();
  }, [fetchWords]);

  const handleInputSubmit = async (
    e: React.KeyboardEvent<HTMLInputElement>
  ) => {
    if (e.key === "Enter" && inputValue.trim()) {
      await addWord(inputValue.trim());
      setInputValue("");
      focusInput();
    }
  };

  const handleWordSelect = async (word: string, hits: number) => {
    await selectWord(word, hits);
  };

  const handleWordDelete = async (word: string) => {
    await deleteWord(word);
  };

  const renderWordButtons = (word: Word) => {
    const buttons = [];
    const wordLength = word.word.length;

    for (let i = 0; i <= wordLength - 1; i++) {
      buttons.push(
        <button
          key={i}
          className="terminal-button mx-1"
          onClick={() => handleWordSelect(word.word, i)}
          disabled={isLoading}
        >
          [{i}]
        </button>
      );
    }

    return buttons;
  };

  return (
    <div className="terminal-container">
      <div className="terminal-screen">
        {/* Área de mensajes */}
        <div className="terminal-header">
          <div className="terminal-line">ROBCO INDUSTRIES (TM) TERMLINK @</div>
          <div className="terminal-line message-line">
            {message && `> ${message}`}
          </div>
        </div>

        {/* Lista de palabras */}
        <div className="terminal-words">
          {words.length > 0 ? (
            words.map((word, index) => (
              <div
                key={`${word.word}-${index}`}
                className="terminal-line word-line"
              >
                <div className="word-info">
                  <span className="word-text">{word.word}</span>
                  <span className="word-matches">({word.matches})</span>
                  <button
                    className="delete-button ml-2"
                    onClick={() => handleWordDelete(word.word)}
                    disabled={isLoading}
                    title="Delete word"
                  >
                    [X]
                  </button>
                </div>
                <div className="word-buttons">{renderWordButtons(word)}</div>
              </div>
            ))
          ) : (
            <div className="terminal-line">NO WORDS LOADED</div>
          )}
        </div>

        {/* Zona de input */}
        <div className="terminal-input-area">
          <div className="terminal-controls">
            <button
              className="terminal-button mr-4"
              onClick={async () => {
                await resetGame();
                await fetchWords();
                focusInput();
              }}
              disabled={isLoading}
            >
              [RESET]
            </button>
            <button
              className="terminal-button"
              onClick={async () => {
                await fetchWords();
                focusInput();
              }}
              disabled={isLoading}
            >
              [REFRESH]
            </button>
          </div>
          <div className="terminal-prompt">
            <span className="prompt-symbol">&gt;</span>
            <input
              ref={inputRef}
              type="text"
              value={inputValue}
              onChange={(e) => setInputValue(e.target.value.toUpperCase())}
              onKeyDown={handleInputSubmit}
              className="terminal-input"
              placeholder="ENTER WORD..."
              disabled={isLoading}
              maxLength={20}
            />
            {isLoading && <span className="loading-indicator">_</span>}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Terminal;
