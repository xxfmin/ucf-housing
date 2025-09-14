"use client";

import {
  createContext,
  useContext,
  useEffect,
  useState,
  ReactNode,
} from "react";

interface User {
  id: number;
  username: string;
  email: string;
  enabled: boolean;
  createdAt: string;
  updatedAt: string;
}

interface AuthContextType {
  user: User | null;
  login: (email: string, password: string) => Promise<void>;
  signup: (
    username: string,
    email: string,
    password: string
  ) => Promise<{ message: string; email: string; userId: number }>;
  verify: (email: string, verificationCode: string) => Promise<void>;
  resendVerification: (email: string) => Promise<void>;
  logout: () => void;
  loading: boolean;
  isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  const isAuthenticated = !!user;
  const API_BASE = "http://localhost:4000";

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      fetchCurrentUser(token);
    } else {
      setLoading(false);
    }
  }, []);

  const fetchCurrentUser = async (token: string) => {
    try {
      console.log(
        "🔍 Fetching current user with token:",
        token?.substring(0, 20) + "..."
      );

      const response = await fetch(`${API_BASE}/users/me`, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      console.log("🔍 /users/me response status:", response.status);
      console.log("🔍 /users/me response headers:", response.headers);

      if (response.ok) {
        // Check if response has content before parsing JSON
        const text = await response.text();
        console.log("🔍 Raw response text:", text);

        if (text.trim() === "") {
          console.error("🔍 Empty response from /users/me");
          localStorage.removeItem("token");
          return;
        }

        try {
          const userData = JSON.parse(text);
          console.log("🔍 User data received:", userData);
          setUser(userData);
        } catch (jsonError) {
          console.error("🔍 JSON parse error:", jsonError);
          console.error("🔍 Response text was:", text);
          localStorage.removeItem("token");
        }
      } else {
        console.error("🔍 Failed to fetch user, status:", response.status);
        const errorText = await response.text();
        console.error("🔍 Error response:", errorText);
        localStorage.removeItem("token");
      }
    } catch (error) {
      console.error("🔍 Network error fetching user:", error);
      localStorage.removeItem("token");
    } finally {
      setLoading(false);
    }
  };

  const login = async (email: string, password: string) => {
    const response = await fetch(`${API_BASE}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.error || "Login failed");
    }

    const { token } = await response.json();
    localStorage.setItem("token", token);
    await fetchCurrentUser(token);
  };

  const signup = async (username: string, email: string, password: string) => {
    const response = await fetch(`${API_BASE}/auth/signup`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, email, password }),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.error || "Signup failed");
    }

    return response.json(); // Returns { message, email, userId }
  };

  const verify = async (email: string, verificationCode: string) => {
    const response = await fetch(`${API_BASE}/auth/verify`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, verificationCode }),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.error || "Verification failed");
    }

    return response.json();
  };

  const resendVerification = async (email: string) => {
    const response = await fetch(
      `${API_BASE}/auth/resend?email=${encodeURIComponent(email)}`,
      {
        method: "POST",
      }
    );

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.error || "Resend failed");
    }

    return response.json();
  };

  const logout = () => {
    localStorage.removeItem("token");
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        signup,
        verify,
        resendVerification,
        logout,
        loading,
        isAuthenticated,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};
