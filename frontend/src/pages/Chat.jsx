import { useState, useRef, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ReactMarkdown from 'react-markdown'; // Added Markdown support
import api from '../services/api';

const Chat = () => {
  const { projectId } = useParams();
  const navigate = useNavigate();
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);
  const messagesEndRef = useRef(null);

  // Auto-scroll logic to stay at the bottom of the conversation
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const handleSend = async (e) => {
    e.preventDefault();
    if (!input.trim()) return;

    const userMsg = { senderRole: 'user', content: input };
    setMessages(prev => [...prev, userMsg]);
    setInput('');
    setLoading(true);

    try {
      const res = await api.post('/chat/send', { projectId, userMessage: input });
      // The content from the AI is saved as markdown text
      setMessages(prev => [...prev, { senderRole: 'assistant', content: res.data }]);
    } catch (err) {
      alert("Error sending message. Check your connection.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="chat-header">
        <button className="back-btn-small" onClick={() => navigate('/dashboard')}>← Back</button>
        <h2>Chat Session</h2>
      </div>

      <div className="chat-container">
        <div className="chat-history">
          {messages.length === 0 && (
            <p style={{textAlign:'center', color:'#999', marginTop: '20px'}}>
               Your session has been reset. Start a new conversation!
            </p>
          )}

          {messages.map((m, i) => (
            <div key={i} className={`message ${m.senderRole}`}>
              {/* FIX: Use ReactMarkdown to render structured content instead of plain text */}
              <ReactMarkdown>{m.content}</ReactMarkdown>
            </div>
          ))}
          
          {loading && <div className="message assistant">Thinking...</div>}
          <div ref={messagesEndRef} />
        </div>

        <form className="chat-input-area" onSubmit={handleSend}>
          <input 
            value={input} 
            onChange={(e) => setInput(e.target.value)} 
            placeholder="Type your message..." 
            disabled={loading}
          />
          <button type="submit" className="btn-new" disabled={loading}>
            {loading ? '...' : 'Send'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default Chat;