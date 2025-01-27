export interface Role {
  id: number;
  description: string;
}

export interface User {
  id: number;
  name: string;
  phoneNumber: string;
  emailAddress: string;
  role: Role;
}
